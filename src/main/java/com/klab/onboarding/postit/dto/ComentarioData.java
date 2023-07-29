package com.klab.onboarding.postit.dto;

public class ComentarioData {

    public  Long id;
    public  String descricao;
    public  Long idTarefa;
    public  Long idUsuario;

    public ComentarioData(){}
    public ComentarioData(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
    public ComentarioData(Long id, String descricao, Long idTarefa) {
        this.id = id;
        this.descricao = descricao;
        this.idTarefa = idTarefa;
    }

    public ComentarioData(Long eachId) {
        this.id = id;
    }
}