package com.klab.onboarding.postit.repository;

import com.klab.onboarding.postit.entity.Comentario;
import com.klab.onboarding.postit.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {


}
