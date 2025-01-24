package com.jair.forohub.domain.topico;

import com.jair.forohub.domain.curso.Categoria;
import com.jair.forohub.domain.respuesta.Respuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    @Query("SELECT COUNT(t) > 0 FROM Topico t WHERE t.id = :topicoId AND t.estatus = 'activo'")
    boolean isTopicoActivo(@Param("topicoId") Long topicoId);

    Page<Topico> findByEstatus(Estatus estatus, Pageable pageable);

    Optional<Topico> findByTituloAndMensaje(String titulo, String mensaje);

    @Query("SELECT r FROM Respuesta r WHERE r.topico.topicoId = :topicoId")
    List<Respuesta> findRespuestaByTopicoId(@Param("topicoId") Long topicoId);

    @Query("SELECT t FROM Topico t WHERE t.curso = :curso")
    List<Topico> findByCurso(Categoria categoria);
}
