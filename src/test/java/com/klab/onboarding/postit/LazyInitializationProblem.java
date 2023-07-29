package com.klab.onboarding.postit;

import com.klab.onboarding.postit.entity.Comentario;
import com.klab.onboarding.postit.entity.Tarefa;
import com.klab.onboarding.postit.entity.Usuario;
import org.hibernate.*;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.lang.reflect.Executable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class LazyInitializationProblem {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private SessionFactory sessionFactory;


	private final String query = """
                    select u
                    	from usuario u
                    where u.id =:id
                    """;
	private final Long id = 8l;

	private static Logger LOGGER = LoggerFactory.logger(LazyInitializationProblem.class);

	@Test
	void shouldThrowLazyInitialization() {
		LOGGER.info("Forcando Lazyinitializ exception");

		var user = entityManager.createQuery(query, Usuario.class)
				.setParameter("id", id)
				.getSingleResult();

		assertThrows(
				org.hibernate.LazyInitializationException.class,
				() -> user.getComentarios().stream()
						.map(Comentario::getDescricao)
						.collect(Collectors.toList())
		);

	}

	@Test
	void sessionWorkAround() {
		LOGGER.info("[Workaround #1 - usando gerenciamento da sessao]");
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		Usuario user = null;
		transaction = session.beginTransaction();
		user = session.get(Usuario.class, id);

		assertFalse(user.getComentarios().isEmpty());

		transaction.commit();
		session.close();
	}

	@Test
	void workAround_dtoProjectionQueryInvertida(){
		LOGGER.info("[Workaround #2.1 - projecao de dto direto da query]");
		var user = entityManager.createQuery("""
                select
                        cm.id as comentario_id,
                        cm.descricao as comentario_descricao,
                        u.id as user_id
                from comentario cm
                right join cm.usuario u
                where u.id =:id
                order by cm.id
                """, Tuple.class)
				.setParameter("id", id)
				.getResultList();

		// Teoricamente aqui construiriamos o objeto de retorno
		// Ou seja: nem haveria Entidade carregadas :: acho q pode ser útil p. read only

		assertFalse(user.isEmpty());
		for (var tuple : user){
			LOGGER.info( "\n" +
					" USER ID: " + tuple.get("user_id") +
					" CMMT ID: " + tuple.get("comentario_descricao")
			);
		}
	}

	@Test
	void workAround_entityGraph(){
		LOGGER.info("[Workaround #3 - @EntityGraph]");
		var entityGraph = entityManager.getEntityGraph("comentariosUsuario");
		var props = Map.<String, Object>of("javax.persistence.loadgraph", entityGraph);
		var userFound = entityManager.find(Usuario.class, id, props);


		assertFalse(userFound.getComentarios().isEmpty());

		LOGGER.info(userFound.getName() + "Autor");
		userFound.getComentarios().forEach(c -> {
			LOGGER.info("CMMT: [ " + c.getDescricao() + "]");
		});


	}
}
