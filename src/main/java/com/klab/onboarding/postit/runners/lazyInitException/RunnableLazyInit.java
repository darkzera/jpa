package com.klab.onboarding.postit.runners.lazyInitException;

import com.klab.onboarding.postit.entity.Tarefa;
import com.klab.onboarding.postit.entity.Usuario;
import com.klab.onboarding.postit.runners.tupleProcessing.TarefaComComentarioTransformer;
import org.hibernate.*;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.Map;
import java.util.stream.Collectors;

//@Configuration
public class RunnableLazyInit implements CommandLineRunner {

    private Logger LOGGER = LoggerFactory.logger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;
    private final String query = """
                    select u
                        from usuario u
                        where u.id =:id
                    """;
    private final Long userId = 8L;
    @Override
    public void run(String... args) throws Exception {


        // Forcando o Lazy init excep.
        try {
            var user = entityManager.createQuery(query, Usuario.class)
                    .setParameter("id", userId)
                    .getSingleResult();

            LOGGER.info(user.getName() + "{"
                    + user.getComentarios().stream().map(cm -> cm.getDescricao())
                    .collect(Collectors.toList())
                    +"}"
            );
            LOGGER.info("EOF");
        } catch (LazyInitializationException lazyInit){
            LOGGER.info(lazyInit.getMessage());
//            workAround_sessionManagment(query);
//            workAround_dtoProjection();
//            workAround_entityGraph();
        }
    }

    private void workAround_dtoProjection(){
        LOGGER.info("[Workaround - projecao de dto direto da query]");
        var user = entityManager.createQuery("""
                select  u.id as usuario_id,
                        u.name as usuario_name,
                        cm.id as comentario_id,
                        cm.descricao as comentario_descricao
                from usuario u
                join fetch u.comentarios cm
                where u.id =:id
                order by cm.id
                """, Tuple.class)
                .setParameter("id", userId)
                .getResultList();

        for (var tuple : user){
            LOGGER.info( "\n" +
                    " USER ID: " + tuple.get("usuario_id") +
                    " CMMT ID: " + tuple.get("comentario_descricao")
            );
        }
    }

    private void workAround_sessionManagment(String query){
        LOGGER.info("[Workaround - session managment]");

    }
//    private void workAround_entityGraph(){
//        var entityGraph = entityManager.getEntityGraph("comentariosTarefa");
//        var props = Map.<String, Object>of("javax.persistence.loadgraph", entityGraph);
//        var tarefa = entityManager.find(Tarefa.class, 12l, props);
//
//        tarefa.getComentarios().forEach(c -> {
//            LOGGER.info("autor cmmt:" + c.getUsuario());
//        }
//
//    }

}
