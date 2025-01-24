package com.jair.forohub.domain.curso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository {
    Optional<Curso> findByNombre(String nombre);
}
