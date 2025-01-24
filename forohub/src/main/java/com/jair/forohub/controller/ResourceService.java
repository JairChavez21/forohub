package com.jair.forohub.controller;

import com.jair.forohub.domain.topico.TopicoRepository;
import com.jair.forohub.domain.usuario.Usuario;
import com.jair.forohub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    public Usuario getUsuarioAutenticado(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String nombre = userDetails.getUsername();
        Usuario usuario = (Usuario) usuarioRepository.findbyNombre(nombre);
        return usuario;
    }

    public boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("Role_ADMIN"));
    }
}
