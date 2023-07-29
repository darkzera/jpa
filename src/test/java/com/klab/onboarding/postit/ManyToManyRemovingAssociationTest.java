package com.klab.onboarding.postit;

import com.klab.onboarding.postit.entity.Comentario;
import com.klab.onboarding.postit.entity.Task;
import com.klab.onboarding.postit.entity.User;
import com.klab.onboarding.postit.entity.Usuario;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ManyToManyRemovingAssociationTest {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private SessionFactory sessionFactory;


	private static Logger LOGGER = LoggerFactory.logger(ManyToManyRemovingAssociationTest.class);

	@Test
	@Transactional
	void t1() {
		var newTask = new Task("N:N Task");
		var user1 = entityManager.find(User.class, 2L);
		var user2 = entityManager.find(User.class, 3L);

		newTask.addAcompanhador(user1);
		newTask.addAcompanhador(user2);
		LOGGER.info("creating scenari");
		newTask = entityManager.merge(newTask);
		remove(newTask.getId());

	}

	public void remove(Long id){

		var createdTask = entityManager
				.createQuery("""
								select t from task t
								join fetch t.acompanhadores tc
								where t.id =: tid
								""", Task.class)
				.setParameter("tid",id)
				.getSingleResult();

		var user1 = entityManager.find(User.class, 2L);

		LOGGER.info("STARTS HERE");
		createdTask.removeAcompanhador(user1);
		LOGGER.info("EOF #1");
		assertEquals(1, createdTask.getAcompanhadores().size());
		LOGGER.info("EOF");
	}

	@Test
	@Transactional
	public void t2(){

//		task1.addAcompanhador(user1);
//		task1.addAcompanhador(user2);
//		task2.addAcompanhador(user2);
//
//		entityManager.persist(task1);
//		entityManager.persist(task2);
//		LOGGER.info("SCENARIO CONST");

		var user1 = entityManager.find(User.class, 2L);
		var user2 = entityManager.find(User.class, 3L);
		var createdTask1 = entityManager
				.find(Task.class, 12l);

		createdTask1.getAcompanhadores().stream().forEach(a -> LOGGER.info(a.getName()));

		LOGGER.info("Remove acompanhador");
		createdTask1.removeAcompanhador(user2);
		var createdTaskAfter = entityManager
				.find(Task.class, 12l);
		createdTaskAfter.getAcompanhadores().stream().forEach(a -> LOGGER.info(a.getName()));


	}

}
