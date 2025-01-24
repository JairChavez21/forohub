package com.jair.forohub.domain.admin;

import com.jair.forohub.domain.usuario.Perfil;
import com.jair.forohub.domain.usuario.RegistroUsuario;
import com.jair.forohub.domain.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "admins")
@Entity(name = "Admin")
@Getter
@RequiredArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Admin extends Usuario {
    public Admin(RegistroUsuario registroUsuario) {
        super(registroUsuario);
        this.perfil = Perfil.ADMIN;
    }
}
