package com.klab.onboarding.postit.repository;

import com.klab.onboarding.postit.dto.projections.UsuarioProjection;
import com.klab.onboarding.postit.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from usuario u join fetch u.comentarios c")
    List<Usuario> findAllUsersWithComments();
}
