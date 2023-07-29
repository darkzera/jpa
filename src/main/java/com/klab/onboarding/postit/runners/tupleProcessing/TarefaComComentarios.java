package com.klab.onboarding.postit.runners.tupleProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class TarefaComComentarios {

    public final static String ID_ALIAS = "tarefa_id";
    public final static String DESCRICAO_ALIAS = "tarefa_descricao";
    private Long id;

    private String descricao;



    private List<ComentarioIdentificador> comentarios = new ArrayList<>();

    public TarefaComComentarios(){}

    public TarefaComComentarios(Long id, String descricao, List<ComentarioIdentificador> comentarios) {
        this.id = id;
        this.descricao = descricao;
        this.comentarios = comentarios;
    }

    public TarefaComComentarios(Object[] tuples,
                                Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.descricao = String.valueOf(tuples[aliasToIndexMap.get(DESCRICAO_ALIAS)]);
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<ComentarioIdentificador> getComentarios() {
        return comentarios;
    }

    @Override
    public String toString() {
        return "TarefaComComentarios[" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", comentarios=" + comentarios.stream().map(c -> c.toString() + "\n").toList() +
                ']';
    }
}
