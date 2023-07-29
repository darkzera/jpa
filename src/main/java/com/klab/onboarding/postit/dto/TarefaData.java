package com.klab.onboarding.postit.dto;

import com.klab.onboarding.postit.entity.Comentario;

import java.util.List;

public class TarefaData {
    public Long id;
    public String descricao;
    public Long idUsuarioResponsavel;
    public List<ComentarioData> comentarios;

    public TarefaData(){}

    public TarefaData(Long id, String descricao, List<ComentarioData> comentarios) {
        this.id = id;
        this.descricao = descricao;
        this.comentarios = comentarios;
    }

    public TarefaData(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
}
