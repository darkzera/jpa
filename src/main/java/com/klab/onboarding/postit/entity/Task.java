package com.klab.onboarding.postit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "task")
@Entity(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rel_task_user",
            joinColumns = { @JoinColumn (name = "id_task")},
            inverseJoinColumns = { @JoinColumn (name = "id_user")}
    )
    List<User> acompanhadores = new ArrayList<>();

    public Task(){}
    public Task(String descricao){
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }


    public List<User> getAcompanhadores() {
        return acompanhadores;
    }

    public void addAcompanhador(User u){
        this.getAcompanhadores().add(u);
        u.getAcompanho().remove(this);
    }

    public void removeAcompanhador(User u){
        this.getAcompanhadores().remove(u);
        u.getAcompanho().remove(this);
    }


}
