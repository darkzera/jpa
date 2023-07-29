package com.klab.onboarding.postit.resource.rest;

import com.klab.onboarding.postit.dto.ComentarioData;
import com.klab.onboarding.postit.dto.TarefaData;
import com.klab.onboarding.postit.dto.UsuarioData;
import com.klab.onboarding.postit.entity.Usuario;
import com.klab.onboarding.postit.repository.UserRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @PersistenceContext
    EntityManager entityManager;

    private final SessionFactory sessionFactory;
    private final UserRepository userRepository;

    public UserController(SessionFactory sessionFactory,
                          UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }

    @GetMapping("/ant")
    public ResponseEntity<List<Usuario>> dontFindAll(){
        return ResponseEntity.ok(
                userRepository.findAll()
        );
    }
    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(
                userRepository.findAllUsersWithComments()
        );
    }

    @PostMapping
    public ResponseEntity<?> criarUser(@RequestBody Usuario u) {
        return ResponseEntity.ok(userRepository.save(u));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdFullDetails(@PathVariable Long id) {

        var u =  entityManager.createQuery("""
                         select distinct u
                         from usuario u
                         left join fetch u.comentarios c
                         where u.id =:id""",
                        Usuario.class)
                .setParameter("id", id)
                .getSingleResult();

        var userData = new UsuarioData(
                u.getId(),
                u.getName(),
                u.getComentarios().stream()
                        .map(c -> new ComentarioData(c.getId(), c.getDescricao()))
                        .collect(Collectors.toList()),
                u.getAcompanho().stream()
                        .map(t -> new TarefaData(t.getId(), t.getDescricao()))
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(userData);
    }
    @GetMapping("/session/{id}")
    private void workAround_sessionManagment(){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Usuario user = null;
        try {
            transaction = session.beginTransaction();
            user = session.find(Usuario.class, 6l);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        user.getComentarios();

    }
}
