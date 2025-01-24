package com.jair.forohub.controller;

import com.jair.forohub.domain.admin.Admin;
import com.jair.forohub.domain.usuario.*;
import com.jair.forohub.infra.exceptions.ValidacionException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<RespuestaUsuario> registrarUsario(
            @RequestBody @Validated RegistroUsuario registroUsuario,
            UriComponentsBuilder uriComponentsBuilder){
        return registrarUsarioWithPerfil(registroUsuario, Perfil.USER, uriComponentsBuilder);
    }

    @PostMapping("/admins")
    public ResponseEntity<RespuestaUsuario> registrarAdmin(
            @RequestBody @Validated RegistroUsuario registroUsuario,
            UriComponentsBuilder uriComponentsBuilder){
        return registrarUsarioWithPerfil(registroUsuario, Perfil.ADMIN, uriComponentsBuilder);
    }

    private ResponseEntity<RespuestaUsuario> registrarUsarioWithPerfil(
            RegistroUsuario registroUsuario,
            Perfil perfil,
            UriComponentsBuilder uriComponentsBuilder) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByNombreOrEmail(registroUsuario.nombre(), registroUsuario.email());
        if (usuarioExistente.isPresent()){
            throw new ValidacionException("El usuario ya está registrado");
        }

        Usuario usuario;
        if (perfil == Perfil.ADMIN){
            usuario = new Admin(registroUsuario);
        } else {
            usuario = new Usuario(registroUsuario);
        }

        String hashedTokken = passwordEncoder.encode(usuario.getPassword());
        usuario.setTokken(hashedTokken);
        usuarioRepository.save(usuario);

        URI url = uriComponentsBuilder.path("/users/{usuarioId}").buildAndExpand(usuario.getUsuarioId()).toUri();
        return ResponseEntity.created(url).body(new RespuestaUsuario(usuario));
    }
}
