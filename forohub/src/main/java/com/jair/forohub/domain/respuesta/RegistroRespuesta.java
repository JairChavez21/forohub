package com.jair.forohub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroRespuesta(
        @NotNull
        Long topicoId,

        @NotBlank
        String mensaje
) {
}
