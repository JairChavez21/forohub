package com.jair.forohub.domain.curso;

public record RespuestaCurso(
        Long cursoId,
        String nombre,
        Categoria categoria
) {
    public RespuestaCurso(Curso curso){
       this(
            curso.getCursoId(),
            curso.getNombre(),
            curso.getCategoria());
    }
}
