package com.klab.onboarding.postit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "usuario")
@Entity(name = "usuario")
@NamedEntityGraph(
        name = "comentariosUsuario",
        attributeNodes = {
                @NamedAttributeNode("comentarios"),
        }
)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "usuario")
    private List<Comentario> comentarios = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "acompanhadores")
    private List<Tarefa> acompanho = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public List<Tarefa> getAcompanho() {
        return acompanho;
    }

}
