package com.jair.forohub.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jair.forohub.domain.topico.Topico;
import com.jair.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity(name = "Respuesta")
@Table (name = "respuestas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "respuestaId")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long respuestaId;
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", referencedColumnName = "topico_id", nullable = false)
    private Topico topico;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", nullable = false)
    private Usuario autor;

    private boolean solucion = false;

    public Respuesta(RegistroRespuesta registroRespuesta, Usuario usuario, Topico topico) {
        this.mensaje = registroRespuesta.mensaje();
        this.topico = topico;
        this.fechaCreacion = LocalDateTime.now();
        this.autor = usuario;
    }

    public void actualizarRespuesta(ActualizacionRespuesta actualizacionRespuesta){
        if (actualizacionRespuesta.mensaje() != null)
            this.mensaje = actualizacionRespuesta.mensaje();
    }
}
