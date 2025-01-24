package com.jair.forohub.domain.usuario;

import com.jair.forohub.domain.respuesta.Respuesta;
import com.jair.forohub.domain.topico.RegistroTopico;
import com.jair.forohub.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "usuarioId")
@Entity(name = "Usuario")
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    private String nombre;
    private String email;
    private String tokken;

    @Enumerated(EnumType.STRING)
    protected Perfil perfil = Perfil.USER;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Topico> topicos = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas = new ArrayList<>();

    public Usuario(RegistroUsuario registroUsuario) {
        this.nombre = registroUsuario.nombre();
        this.email = registroUsuario.email();
        this.tokken = registroUsuario.contrasena();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("Role_"+perfil.name()));
    }

    @Override
    public String getPassword() {
        return tokken;
    }

    @Override
    public String getUsername() {
        return nombre;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
