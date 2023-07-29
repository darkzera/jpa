package com.klab.onboarding.postit.runners.tupleProcessing;

import java.util.Map;
import java.util.Objects;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class ComentarioIdentificador {

    private final static String ID_ALIAS = "comentario_id";
    private final static String DESCRICAO_ALIAS = "comentario_descricao";

    private Long id;

    private String descricao;

    public ComentarioIdentificador(Long id,
                                   String descricao){
            this.id = id;
            this.descricao = descricao;
    }
    public ComentarioIdentificador(Object[] tuples,
                                   Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.descricao = (tuples[aliasToIndexMap.get(DESCRICAO_ALIAS)]).toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComentarioIdentificador)) return false;
        ComentarioIdentificador that = (ComentarioIdentificador) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDescricao(), that.getDescricao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getDescricao()
        );
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return "ComentarioIdentificador[" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ']';
    }
}
