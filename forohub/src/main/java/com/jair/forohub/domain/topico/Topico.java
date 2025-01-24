package com.jair.forohub.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jair.forohub.domain.curso.Categoria;
import com.jair.forohub.domain.curso.Curso;
import com.jair.forohub.domain.respuesta.Respuesta;
import com.jair.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import javax.swing.plaf.InsetsUIResource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "topicoId")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicoId;
    private String titulo;
    private String mensaje;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fechaCreacion;

    private Estatus estatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", referencedColumnName = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "topico", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas = new ArrayList<>();

    public Topico(RegistroTopico datosRegistro, Usuario usuario) {
        this.titulo = datosRegistro.titulo();
        this.mensaje = datosRegistro.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.estatus = Estatus.ACTIVO;
        this.autor = usuario;
    }

    public void actualizarTopico(ActualizarTopico actualizarTopico){
        if (actualizarTopico.titulo() != null)
            this.titulo = actualizarTopico.titulo();
        if (actualizarTopico.mensaje() != null)
            this.mensaje = actualizarTopico.mensaje();
    }

    public void borrarTopico() {
        this.estatus = Estatus.BORRADO;
    }

    public void aprovarTopico() {
        this.estatus = Estatus.ACTIVO;
    }

    public void cerrarTopico() {
        this.estatus = Estatus.CERRADO;
    }
}
