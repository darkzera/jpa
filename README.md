- Docker
  - Adminer
  - PostgreSQL
   
- JPA
- REST api

```jsx
$ docker compose up -d
$ mvn spring-boot:run 
```

## POC - JPA Hibernate algumas práticas e tradeoffs
Este projeto tem como objetivo analisar os tradeoffs de utilizar JPA Hibernate e buscar saídas para anti-patterns mais conhecidos e relacionados ao nosso contexto.

A intenção é demonstrar os potenciais riscos e limitações dessas abordagens.

Mapeamento de relacionamentos:
- @OneToMany
- @ManyToMany


Pontos de atençao:
- Manipulaçao desnecessaria de métodos de acesso com conteúdos nao carregados em Entidades
- Objetos de dados (dto)
- Atençao ao contrato da api rest pq pode gerar um universo infinito dos objetos citados acima
- Builders, mappers e projeçao diretamente da camada de acesso BD

todo:
- Objetos nao-entidades a partir das JPQL -> Tuplas
- findAll antipattern
- @ManyToMany @CollectionElement <Map, Entidade>


- https://docs.oracle.com/cd/E14571_01/apirefs.1111/e13946/ejb3_overview_query.html
- https://stackoverflow.com/questions/4929243/clarifying-terminology-what-does-hydrating-a-jpa-or-hibernate-entity-mean-wh

---

Basicamente ao fazer isso:

```jsx
public ResponseEntity<?> qualquerCoisa(...) {
	...
	return ResponseEntity.ok(algumaEntidadeJPA)
}
```
O @ResponseEntity chama todos os getXXX() da classe, e ai achoque gera recursao inifinita e dá stackoverflow p. escrever a resposta.



API rest
localhost:8080/




### join fetch p. obter dados que serao utilizados em qualquer método de acesso

```jsx
Tarefa task = em                                                                          
		.createQuery("select t from tarefa t where t.id =:id", Tarefa.class)
		.setParameter("id", 1l)
		.getSingleResult();
task.getComentarios().forEach(c -> LOGGER.info(c.getId()));
```
```jsx
Hibernate: 
    select
        tarefa0_.id as id1_2_,
        tarefa0_.descricao as descrica2_2_,
        tarefa0_.id_st_tarefa as id_st_ta3_2_,
        tarefa0_.nome as nome4_2_,
        tarefa0_.id_usuario_responsavel as id_usuar5_2_ 
    from
        tarefa tarefa0_ 
    where
        tarefa0_.id=?
Hibernate: 
    select
        comentario0_.tarefa_id as tarefa_i4_0_0_,
        comentario0_.id as id1_0_0_,
        comentario0_.id as id1_0_1_,
        comentario0_.descricao as descrica2_0_1_,
        comentario0_.tarefa_id as tarefa_i4_0_1_,
        comentario0_.titulo as titulo3_0_1_,
        comentario0_.id_usuario as id_usuar5_0_1_ 
    from
        comentario comentario0_ 
    where
        comentario0_.tarefa_id=?
19:00:27 INFO  [main] c.k.o.postit.PostitApplicationTests - 1
19:00:27 INFO  [main] c.k.o.postit.PostitApplicationTests - 2
19:00:27 INFO  [main] c.k.o.postit.PostitApplicationTests - 3
```

```jsx
.createQuery("select t from tarefa t join fetch t.comentarios c where t.id =:id", Tarefa.class)
ou
@Query("select t from tarefa t join fetch t.comentarios c where t.id =:id")
Tarefa findByIdWithComments(@Param("id")
```
```jsx
Hibernate:
select
    tarefa0_.id as id1_2_0_,
    comentario1_.id as id1_0_1_,
    tarefa0_.descricao as descrica2_2_0_,
    tarefa0_.id_st_tarefa as id_st_ta3_2_0_,
    tarefa0_.nome as nome4_2_0_,
    tarefa0_.id_usuario_responsavel as id_usuar5_2_0_,
    comentario1_.descricao as descrica2_0_1_,
    comentario1_.tarefa_id as tarefa_i4_0_1_,
    comentario1_.titulo as titulo3_0_1_,
    comentario1_.id_usuario as id_usuar5_0_1_,
    comentario1_.tarefa_id as tarefa_i4_0_0__,
    comentario1_.id as id1_0_0__
from
    tarefa tarefa0_
inner join
    comentario comentario1_
    on tarefa0_.id=comentario1_.tarefa_id
where
    tarefa0_.id=?
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 1
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 2
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 3
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 4
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 1
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 2
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 3
19:03:56 INFO  [main] c.k.o.postit.PostitApplicationTests - 4
```
---
### getReferenceById vs findById anti-pattern
```jsx
Tarefa tarefa = new Tarefa(
        t.descricao,
        userRepository.getReferenceById(t.idUsuarioResponsavel));

tarefaRepository.save(tarefa);
```
```jsx
Hibernate: 
    insert 
    into
        tarefa
        (descricao, id_st_tarefa, nome, id_usuario_responsavel) 
    values
        (?, ?, ?, ?)
```
```
Tarefa tarefa = new Tarefa(
        t.descricao,```
        userRepository.findById(t.idUsuarioResponsavel).get());
```
```jsx
Hibernate: 
    select
        usuario0_.id as id1_3_0_,
        usuario0_.email as email2_3_0_,
        usuario0_.name as name3_3_0_ 
    from
        usuario usuario0_ 
    where
        usuario0_.id=?
Hibernate: 
    insert 
    into
        tarefa
        (descricao, id_st_tarefa, nome, id_usuario_responsavel) 
    values
        (?, ?, ?, ?)

```
---
# Principais referencias
[thorben-janssen.com](https://thorben-janssen.com/dto-projections/)

[mihalcea.com](https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/)

---
