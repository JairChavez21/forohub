package com.jair.forohub.domain.curso;

import com.jair.forohub.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "Curso")
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cursoId")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "name", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Topico> topicos;

    public Curso (RegistrarCurso registrarCurso) {
        this.nombre = registrarCurso.nombre();
        this.categoria = registrarCurso.categoria();
    }
}
