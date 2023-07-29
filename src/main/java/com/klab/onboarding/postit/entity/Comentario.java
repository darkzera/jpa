package com.klab.onboarding.postit.entity;

import javax.persistence.*;

@Table(name = "comentario")
@Entity(name = "comentario")
public class Comentario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tarefa tarefa;

    @JoinColumn(name = "id_usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    public Comentario(){}
    public Comentario(String desc){
        this.descricao = desc;
    }
    public Comentario(String desc, Usuario usuario){
        this.descricao = desc;
        this.usuario = usuario;
    }
    public Tarefa getTarefa() {
        return tarefa;
    }
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    @Override
    public String toString() {
        return "Comentario[" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ']';
    }
}
