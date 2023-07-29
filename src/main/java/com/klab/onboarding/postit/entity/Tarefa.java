package com.klab.onboarding.postit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tarefa")
@Entity(name = "tarefa")

public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_responsavel")
    Usuario responsavel;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rel_tarefa_usuario",
            joinColumns = { @JoinColumn (name = "id_tarefa")},
            inverseJoinColumns = { @JoinColumn (name = "id_usuario")}
    )
    List<Usuario> acompanhadores = new ArrayList<>();

    @OneToMany(mappedBy = "tarefa",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    public List<Comentario> comentarios = new ArrayList<>();

    public Tarefa(){}
    public Tarefa(Long id, Usuario responsavel){
        this.responsavel = responsavel;
    }

    public Tarefa(String descricao, Usuario responsavel){
        this.descricao = descricao;
        this.responsavel = responsavel;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public List<Usuario> getAcompanhadores() {
        return acompanhadores;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void addComentario(Comentario c){
        this.getComentarios().add(c);
        c.setTarefa(this);
    }

    public void removeComentario(Comentario c){
        this.getComentarios().remove(c);
        c.setTarefa(null);
    }
}
