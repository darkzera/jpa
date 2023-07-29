package com.klab.onboarding.postit;

import com.klab.onboarding.postit.entity.Comentario;
import com.klab.onboarding.postit.entity.Tarefa;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootTest
class PostitApplicationTests {
	@PersistenceContext
	private EntityManager em;

	private static Logger LOGGER = LoggerFactory.logger(PostitApplicationTests.class);
	@Test
	@Transactional
	void contextLoads() {
		Tarefa task = em
				.createQuery("select t from tarefa t join fetch t.comentarios c where t.id =:id", Tarefa.class)
				.setParameter("id", 1l)
				.getSingleResult();

		task.getComentarios().forEach(c -> LOGGER.info(c.getId()));

	}


}
