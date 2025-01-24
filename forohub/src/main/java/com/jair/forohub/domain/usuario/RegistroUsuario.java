package com.jair.forohub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroUsuario(
        @NotBlank(message = "El nombre de usuario no puede estar vacío.")
        @Size(min = 3, max = 20, message = "Máximo 20 caracteres por nombre de usuario.")
        String nombre,

        @NotBlank(message = "El correo electrónico no puede estar vacío.")
        @Email(message = "El email debe estar en un formato válido.")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía.")
        @Size(min = 6, message = "La contraseña debe tener al menos 8 caracteres.")
        String contrasena
) {
}
