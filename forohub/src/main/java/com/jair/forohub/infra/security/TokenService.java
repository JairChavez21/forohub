package com.jair.forohub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jair.forohub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Value("${forohub.security.secret}")
    private String forohubSecret;

    public String generateToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(forohubSecret);
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(user.getUsername())
                    .withClaim("userId", user.getUsuarioId())
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())) // Incluye los roles
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException();
        }
    }

    private Instant generateExpirationDate() {

        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException("El token no puede estar vacío");
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(forohubSecret); // validando firma
            verifier = JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
        }
        if (verifier.getSubject() == null) {
            throw new RuntimeException("Verificador inválido.");
        }
        return verifier.getSubject();
    }

    // Método para extraer los roles del token:
    public List<String> getRoles(String token) {
        if (token == null) {
            throw new RuntimeException("El token está vacío.");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(forohubSecret); // Usa tu clave secreta.
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token);

            // Extrae los roles desde las claims:
            String[] rolesArray = decodedJWT.getClaim("roles").asArray(String.class);
            return rolesArray != null ? Arrays.asList(rolesArray) : List.of();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error al verificar el token: " + exception.getMessage());
        }
    }
}
