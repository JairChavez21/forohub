package com.jair.forohub.domain.topico;

public record ActualizarTopico(
        Long topicoId,
        String titulo,
        String mensaje
) {
}
