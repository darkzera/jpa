package com.klab.onboarding.postit.dto.projections;

import java.util.List;

public interface TarefaProjection {

    Long getId();
    String getDescricao();
    UsuarioProjection getResponsavel();

    List<ComentarioProjection> getComentarios();

}
