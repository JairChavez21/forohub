package com.jair.forohub.domain.usuario;

public record RespuestaUsuario(
        Long usuarioID,
        String nombre,
        String email
) {
    public RespuestaUsuario(Usuario usuario){
        this(usuario.getUsuarioId(), usuario.getNombre(), usuario.getEmail());
    }
}
