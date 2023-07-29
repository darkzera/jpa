package com.klab.onboarding.postit.runners.tupleProcessing;

import org.hibernate.transform.ResultTransformer;

import java.util.*;

import static org.aspectj.runtime.internal.Conversions.longValue;

public class TarefaComComentarioTransformer implements ResultTransformer {

    Map<Long, TarefaComComentarios> tarefaCommentarioMapper =
            new HashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

        Long tarefaId = longValue(tuple[aliasToIndexMap.get(TarefaComComentarios.ID_ALIAS)]);

        TarefaComComentarios tarefa = tarefaCommentarioMapper
                .computeIfAbsent(tarefaId,
                        id -> new TarefaComComentarios(tuple, aliasToIndexMap)
                );

        tarefa.getComentarios().add(new ComentarioIdentificador(
                tuple, aliasToIndexMap
        ));

        return tarefa;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(tarefaCommentarioMapper.values());
    }

    public  Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i], i);
        }

        return aliasToIndexMap;
    }


}
