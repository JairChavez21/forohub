package com.jair.forohub.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RespuestaTopico(
        Long topicoId,
        String titulo,
        String mensaje,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        String fechaCreacion,
        String estatus,
        Long usuarioId,
        String autor,
        String curso
) {
    public RespuestaTopico(Topico topico){
        this (topico.getTopicoId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion().toString(),
                topico.getEstatus().toString(),
                topico.getAutor().getUsuarioId(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre());
    }
}
