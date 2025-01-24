package com.jair.forohub.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RespuestaRespuesta(
        Long respuestaId,
        String mensaje,
        Long topicoId,
        String topico,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        String fechaCreacion,

        Long usuarioId,
        String autor,
        boolean solucion
) {
    public RespuestaRespuesta(Respuesta respuesta) {
        this
                (
                        respuesta.getRespuestaId(),
                        respuesta.getMensaje(),
                        respuesta.getTopico().getTopicoId(),
                        respuesta.getTopico().getTitulo(),
                        respuesta.getFechaCreacion().toString(),
                        respuesta.getAutor().getUsuarioId(),
                        respuesta.getAutor().getNombre(),
                        respuesta.isSolucion()
                );
    }
}
