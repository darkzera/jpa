package com.klab.onboarding.postit.dto;

import com.klab.onboarding.postit.entity.Comentario;
import com.klab.onboarding.postit.entity.Tarefa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioData {

    public Long id;
    public String name;

    public String email;

    public List<ComentarioData> comentarios = new ArrayList<>();

    public List<TarefaData> acompanho = new ArrayList<>();

    public UsuarioData(Object... args){
       this.id = 1l;
    }
    public UsuarioData(Long id,
                       String name,
                       List<Long> comentariosId) {
        this.id = id;
        this.name = name;
        this.comentarios = comentariosId.stream().map(eachId -> new ComentarioData(eachId)).collect(Collectors.toList());
    }
    public UsuarioData(){}
    public UsuarioData(Long id,
                       String name,
                       List<ComentarioData> comentarios,
                       List<TarefaData> acompanho) {
        this.id = id;
        this.name = name;
        this.comentarios = comentarios;
        this.acompanho = acompanho;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<ComentarioData> getComentarios() {
        return comentarios;
    }

    public List<TarefaData> getAcompanho() {
        return acompanho;
    }

}
