package com.klab.onboarding.postit.runners;

import com.klab.onboarding.postit.entity.Task;
import com.klab.onboarding.postit.entity.User;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Configuration
public class ListSetRunnable implements CommandLineRunner {

    private static Logger LOGGER = LoggerFactory.logger(ListSetRunnable.class);
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        entityManager.persist(new User("Lucas", "lu@cas.net"));
//        entityManager.persist(new User("Luiz", "luiz@net.com"));
//        entityManager.persist(new User("Almeidinha", "al@meida.net"));
//
//        var task = new Task("Primeira");
//        var createdTask = entityManager.merge(task);
//        System.out.println("---");
//        var listUsers =  entityManager.createQuery("""
//                select u from users u
//                """, User.class)
//                .getResultList();
//        listUsers.stream().forEach(u -> createdTask.addAcompanhador(u));
//
//        createdAltTask.addAcompanhador(listUsers.stream()
//                        .filter(c -> c.getEmail().equalsIgnoreCase("lucas"))
//                        .findFirst()
//                        .orElseThrow(NoSuchElementException::new));

        var user1 = entityManager.find(User.class, 13L);
        var createdTask1 = entityManager.find(Task.class, 12l);
        createdTask1.removeAcompanhador(user1);



        createdTaskAfter.getAcompanhadores();


    }
}
