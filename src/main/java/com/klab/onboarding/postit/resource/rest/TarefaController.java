package com.klab.onboarding.postit.resource.rest;

import com.klab.onboarding.postit.dto.ComentarioData;
import com.klab.onboarding.postit.dto.TarefaData;
import com.klab.onboarding.postit.entity.Comentario;
import com.klab.onboarding.postit.entity.Tarefa;
import com.klab.onboarding.postit.repository.ComentarioRepository;
import com.klab.onboarding.postit.repository.TarefaRepository;
import com.klab.onboarding.postit.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    @PersistenceContext
    EntityManager entityManager;

    private final TarefaRepository tarefaRepository;
    private final UserRepository userRepository;
    private final ComentarioRepository comentarioRepository;

    public TarefaController(TarefaRepository tarefaRepository,
                            UserRepository userRepository,
                            ComentarioRepository comentarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.userRepository = userRepository;
        this.comentarioRepository = comentarioRepository;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody TarefaData t) {

        var tarefa = tarefaRepository.save(new Tarefa(
                        t.descricao,
                        userRepository.getReferenceById(t.idUsuarioResponsavel))
        );

        var tarefaData = new TarefaData(tarefa.getId(), tarefa.getDescricao());

        return ResponseEntity.ok(tarefaData);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<TarefaData> tarefas = tarefaRepository.findAll().stream()
                .map(task -> new TarefaData(task.getId(), task.getDescricao()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tarefas);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findTarefaById(@PathVariable Long id) {
       Tarefa t = tarefaRepository.findByIdWithComments_(id).get();

        ResponseEntity.ok(new TarefaData(
                t.getId(),
                t.getDescricao(),
                t.getComentarios().stream()
                        .map(c -> new ComentarioData(c.getId(), c.getDescricao()))
                        .collect(Collectors.toList())));

        return ResponseEntity.ok(
                tarefaRepository.findByIdWithComments(id)
        );

    }

    @PostMapping("/{id}")
    public ResponseEntity<?> incluirComentario(@PathVariable Long id,
                                               @RequestBody ComentarioData c){


        final var tarefaRef = tarefaRepository.getReferenceById(id);
        final var userRef = userRepository.getReferenceById(c.idUsuario);
        final var novoCmmt = new Comentario( c.descricao, userRef);

        novoCmmt.setTarefa(tarefaRef);
        comentarioRepository.save(novoCmmt);

        final var commtData  = new ComentarioData(
                novoCmmt.getId(), novoCmmt.getDescricao(), novoCmmt.getTarefa().getId()
        );


        return ResponseEntity.ok(commtData);
    }



}
