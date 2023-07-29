package com.klab.onboarding.postit.repository;

import com.klab.onboarding.postit.dto.projections.TarefaProjection;
import com.klab.onboarding.postit.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("select distinct t from tarefa t left join fetch t.comentarios c where t.id =:id")
    Optional<Tarefa> findByIdWithComments_(@Param("id") Long id);

    @Query("select distinct t from tarefa t left join fetch t.comentarios c where t.id =:id")
    Optional<TarefaProjection> findByIdWithComments(@Param("id") Long id);
}
