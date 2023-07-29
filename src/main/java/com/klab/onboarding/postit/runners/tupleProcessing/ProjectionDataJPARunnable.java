package com.klab.onboarding.postit.runners.tupleProcessing;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

//@Configuration
public class ProjectionDataJPARunnable implements CommandLineRunner {


    private static Logger LOGGER = LoggerFactory.logger(ProjectionDataJPARunnable.class);
    @PersistenceContext
    private EntityManager em;


    @Override
    public void run(String... args) throws Exception {

        List<TarefaComComentarios> result = em.createQuery("""
                select distinct t.id as tarefa_id,
                        t.descricao as tarefa_descricao,
                        cm.id as comentario_id,
                        cm.descricao as comentario_descricao
                from comentario cm
                left join cm.tarefa t
                where t.id =:idTarefa
                order by cm.id
                """)
                .setParameter("idTarefa", 11L)
                .unwrap(Query.class)
                .setResultTransformer(new TarefaComComentarioTransformer())
                .list();

        LOGGER.info(result.toString());

    }
}
