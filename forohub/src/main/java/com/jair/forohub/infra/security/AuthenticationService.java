package com.jair.forohub.infra.security;

import com.jair.forohub.domain.usuario.Usuario;
import com.jair.forohub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String nombre) throws UsernameNotFoundException {
        System.out.println("Buscando usuario: " + nombre);
        Usuario usuario = (Usuario) userRepository.findbyNombre(nombre);
        System.out.println("Resultado de la búsqueda: " + usuario);

        if (usuario == null) {
            throw new UsernameNotFoundException("El usuario " + usuario.getNombre() + " no ha sido encontrado");
        }

        System.out.println("Usuario encontrado: " + usuario);
        return usuario;
    }
}
