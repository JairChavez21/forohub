package com.jair.forohub.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarCurso(
        @NotBlank
        String nombre,

        @NotNull
        Categoria categoria) {
}
