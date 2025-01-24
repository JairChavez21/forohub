package com.jair.forohub.controller;

import com.jair.forohub.domain.usuario.AutenticacionUsuario;
import com.jair.forohub.domain.usuario.Usuario;
import com.jair.forohub.infra.security.TokenJWTdto;
import com.jair.forohub.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Login de Usuario")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity AutenticarUsuario(@RequestBody @Valid AutenticacionUsuario autenticacionUsuario){
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                autenticacionUsuario.nombre(),
                autenticacionUsuario.contrasena());
        var usuarioAutenticado = authenticationManager.authenticate(authentication);
        var tokenJWT = tokenService.generateToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new TokenJWTdto(tokenJWT));
    }
}
