package com.jair.forohub.infra.security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,
                                "/login", "/usuarios", "/usuarios/admins")
                        .permitAll()
                        .requestMatchers("/error")
                        .permitAll()
                        // Rutas exclusivas para administradores:
                        .requestMatchers(HttpMethod.PUT, "/topicos/{topicoId}/estatus")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/topicos/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/cursos")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/respuestas/{respuestaId}/solucion")
                        .hasRole("ADMIN")
                        // Rutas para usuarios con rol USER o ADMIN:
                        .requestMatchers(HttpMethod.POST, "/topicos", "/respuestas")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/topicos/**", "/respuestas/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/topicos", "/topicos/**",
                                "/respuestas", "/respuestas/**", "/respuestas/{topicoId}")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/swagger-ui.html",
                                "/v3/api-docs/**", "/swagger-ui/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                               authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
