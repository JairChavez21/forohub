package com.jair.forohub.controller;

import com.jair.forohub.domain.curso.Curso;
import com.jair.forohub.domain.curso.CursoRepository;
import com.jair.forohub.domain.curso.RegistrarCurso;
import com.jair.forohub.domain.curso.RespuestaCurso;
import com.jair.forohub.infra.exceptions.ValidacionException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespuestaCurso> registrarCurso(@RequestBody @Validated RegistrarCurso registrarCurso,
                                                         UriComponentsBuilder uriComponentsBuilder){
        Optional<Curso> cursoExistente = cursoRepository.findByNombre(registrarCurso.nombre());
        if (cursoExistente.isPresent()) {
            throw new ValidacionException("Este curso ya existe en la base de datos");
        }
        Curso curso = new Curso(registrarCurso);
        cursoRepository.save(curso);
        URI url = uriComponentsBuilder.path("/cursos/{cursoId}")
                .buildAndExpand(curso.getCursoId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).body(new RespuestaCurso(curso));
    }

    @GetMapping
    public ResponseEntity<Page<RespuestaCurso>> listarTodosLosCursos(
            @PageableDefault(size = 10)Pageable pageable){
        Page<Curso> cursos = cursoRepository.findAll(pageable);
        Page<RespuestaCurso> respuestaCursos = cursos.map(RespuestaCurso::new);
        return ResponseEntity.ok(respuestaCursos);
    }
}
