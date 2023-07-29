package com.klab.onboarding.postit.dto.projections;

import java.util.List;

public interface UsuarioProjection {

    Long getId();
    String getName();
    String getEmail();
    List<ComentarioProjection> getComentarios();

}
