package com.jair.forohub.controller;

import com.jair.forohub.domain.curso.Curso;
import com.jair.forohub.domain.curso.CursoRepository;
import com.jair.forohub.domain.topico.*;
import com.jair.forohub.domain.usuario.Usuario;
import com.jair.forohub.domain.usuario.UsuarioRepository;
import com.jair.forohub.infra.bussines.BusinessServices;
import com.jair.forohub.infra.exceptions.ValidacionException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private BusinessServices businessServices;

    @Autowired
    private ResourceService resourceService;

    @PostMapping
    public ResponseEntity<RespuestaTopico> registrarTopico(
            @RequestBody @Valid RegistroTopico registroTopico,
            UriComponentsBuilder uriComponentsBuilder){

        businessServices.validarPostTime();

        Optional<Topico> existeTopico = topicoRepository.findByTituloAndMensaje(registroTopico.titulo(), registroTopico.mensaje());

        if (existeTopico.isPresent()){
            throw new ValidacionException("Ya existe un Topico con ese titulo y contenido");
        }

        Curso curso = cursoRepository.findByNombre(registroTopico.categoria().name())
                .orElseThrow(()-> new ValidacionException("El curso especificado no existe"));

        Usuario usuario = resourceService.getUsuarioAutenticado();

        Topico topico = new Topico(registroTopico, usuario);
        topico.setAutor(usuario);
        topico.setCurso(curso);
        topico.setEstatus(Estatus.ACTIVO);
        topicoRepository.save(topico);

        URI url = uriComponentsBuilder.path("/topicos/{topicoId}")
                .buildAndExpand(topico.getTopicoId()).toUri();

        return ResponseEntity.created(url).body(new RespuestaTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<RespuestaTopico>> listaDeTopicos(
            @RequestParam(required = false, defaultValue = "ACTIVO") String estatus,
            @PageableDefault(size = 6)
            Pageable pageable){

        Estatus estatus1;
        try {
            estatus1 = Estatus.valueOf(estatus.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new ValidacionException("El estatus rpoporcionado no es válido, por favor usa: ACTIVO, CERRADO, BORRADO");
        }

        if ((estatus1 == Estatus.CERRADO || estatus1 == Estatus.BORRADO) && !resourceService.isAdmin()){
            throw new AccessDeniedException("Usted no cuenta con los permisos necesarios para ver esta lista de contenido");
        }

        Page<Topico> topicos = topicoRepository.findByEstatus(estatus1, pageable);
        Page<RespuestaTopico> respuestaTopicos = topicos.map(RespuestaTopico::new);
        return ResponseEntity.ok(respuestaTopicos);
    }

    @PutMapping ("/{topicoId}")
    @Transactional
    public ResponseEntity<RespuestaTopico> actualizaTopicoPorId(@PathVariable Long topicoId,
                                                                @RequestBody ActualizarTopico actualizarTopico){
        try {
            Usuario usuario = resourceService.getUsuarioAutenticado();
            Topico topico = topicoRepository.getReferenceById(topicoId);

            if (!topico.getAutor().getNombre().equals(usuario.getNombre())){
                throw new SecurityException("Usted no tiene permiso para editar este topico");
            }

            businessServices.validarEditarTime(topico.getFechaCreacion());

            topico.actualizarTopico(actualizarTopico);

            topicoRepository.save(topico);

            return ResponseEntity.ok(new RespuestaTopico(topico));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("El topico al que estás tratando de acceder no existe");
        }
    }

    @DeleteMapping("/{topicoId}")
    @Secured("Role_ADMIN")
    @Transactional
    public ResponseEntity deleteTopicoById(@PathVariable Long topicoId){
        Topico topico = topicoRepository.getReferenceById(topicoId);
        topico.setEstatus(Estatus.BORRADO);
        topico.borrarTopico();
        return ResponseEntity.noContent().build();
    }
}
