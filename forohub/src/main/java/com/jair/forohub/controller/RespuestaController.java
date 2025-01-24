package com.jair.forohub.controller;

import com.jair.forohub.domain.respuesta.*;
import com.jair.forohub.domain.topico.Estatus;
import com.jair.forohub.domain.topico.RespuestaTopico;
import com.jair.forohub.domain.topico.Topico;
import com.jair.forohub.domain.topico.TopicoRepository;
import com.jair.forohub.domain.usuario.Usuario;
import com.jair.forohub.domain.usuario.UsuarioRepository;
import com.jair.forohub.infra.bussines.BusinessServices;
import com.jair.forohub.infra.exceptions.AccionNoAutorizadaException;
import com.jair.forohub.infra.exceptions.ValidacionException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {
    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private BusinessServices businessServices;

    @Autowired
    private ResourceService resourceService;

    @PostMapping
    public ResponseEntity<RespuestaRespuesta> registrarRespuesta(
            @RequestBody @Valid RegistroRespuesta registroRespuesta,
            UriComponentsBuilder uriComponentsBuilder){
        businessServices.validarPostTime();

        Optional<Topico> existeTopico = topicoRepository.findById(registroRespuesta.topicoId());
        if (existeTopico.isEmpty()) {
            throw new ValidacionException("El topico no está disponible");
        }

        if (!topicoRepository.isTopicoActivo(registroRespuesta.topicoId())){
            throw new ValidacionException("Este topico no está activo, es decir, que ya se ha solucionado o ha sido borrado");
        }

        Usuario usuario = resourceService.getUsuarioAutenticado();

        Respuesta respuesta = new Respuesta(
                registroRespuesta,
                usuario,
                existeTopico.get());

        respuesta.setAutor(usuario);
        respuesta.setTopico(existeTopico.get());

        respuestaRepository.save(respuesta);

        URI url = uriComponentsBuilder.path("/respuestas/{respuestaId}").buildAndExpand(respuesta.getRespuestaId()).toUri();

        return ResponseEntity.created(url).body(new RespuestaRespuesta(respuesta));
    }

    @PutMapping("/{respuestaId}")
    @Transactional
    public ResponseEntity<RespuestaRespuesta> actualizarRespuestaById(@PathVariable Long respuestaId, @RequestBody ActualizacionRespuesta actualizacionRespuesta){
        try {
            Usuario usuario = resourceService.getUsuarioAutenticado();
            Respuesta respuesta = respuestaRepository.getReferenceById(respuestaId);
            if (!respuesta.getAutor().equals(usuario)){
                throw new SecurityException("Solo el autor de la respuesta puede editar dicha respuesta");
            }
            businessServices.validarEditarTime(respuesta.getFechaCreacion());
            respuesta.actualizarRespuesta(actualizacionRespuesta);
            respuestaRepository.save(respuesta);

            return ResponseEntity.ok(new RespuestaRespuesta(respuesta));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("La respuesta con el id proporcionado no existe");
        }
    }

    @GetMapping
    public ResponseEntity<List<RespuestaRespuesta>> obtenerRespuestaPorTopicoId(@RequestParam Long topicoId){
       List<Respuesta> respuestas = topicoRepository.findRespuestaByTopicoId(topicoId);
       List<RespuestaRespuesta> respuestaFinal = respuestas.stream().map(RespuestaRespuesta::new).toList();
       return ResponseEntity.ok(respuestaFinal);
    }

    @PatchMapping("/{respuestaId}/solution")
    @Secured("Role_ADMIN")
    @Transactional
    public ResponseEntity solucionado(@PathVariable Long respuestaId){

        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("Role_ADMIN"))){
            throw new AccionNoAutorizadaException("Usted no tiene permisos para realizar cambios");
        }

        Respuesta respuesta = respuestaRepository.findById(respuestaId)
                .orElseThrow(()->new EntityNotFoundException("Respuesta no encontrada"));

        if (respuesta.isSolucion()) {
            throw new IllegalStateException("Esta respuesta ysa está marcada como solucion");
        }

        respuesta.setSolucion(true);

        Topico topico = respuesta.getTopico();
        if (!Estatus.CERRADO.equals(topico.getEstatus())){
            topico.cerrarTopico();
        }

        respuestaRepository.save(respuesta);
        topicoRepository.save(topico);
        return ResponseEntity.ok("La respuesta fue seleccionada como solución y el topico se ha cerrado correctamente");
    }
}
