package com.alura.apiforo.controller;

import com.alura.apiforo.domain.curso.CursoRepository;
import com.alura.apiforo.domain.respuesta.*;
import com.alura.apiforo.domain.topico.*;
import com.alura.apiforo.domain.usuario.DatosMostrarUsuario;
import com.alura.apiforo.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topicos")
@Tag(name = "Topicos", description = "Operaciones relacionadas con topicos")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final CursoRepository cursoRepository;
    private final RespuestaRepository respuestaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public TopicoController(TopicoRepository topicoRepository, CursoRepository cursoRepository,
                            RespuestaRepository respuestaRepository,
                            UsuarioRepository usuarioRepository){
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
        this.respuestaRepository = respuestaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @Operation(summary = "Registrar un topico")
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder){
        var curso = cursoRepository.getReferenceById(datosRegistroTopico.id_curso());
        var autor = usuarioRepository.getReferenceById(datosRegistroTopico.id_autor());
        var topico = topicoRepository.save(new Topico(datosRegistroTopico, curso, autor));
        var datosMostrarUsuario = new DatosMostrarUsuario(autor.getId(), autor.getNombre());
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(), datosMostrarUsuario, curso);
        URI url = uriComponentsBuilder.path("/topicos/{idTopico}").buildAndExpand(topico.getId().toString()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }
    @PostMapping("/{idTopico}/respuestas")
    @Operation(summary = "Registrar una respuesta")
    public ResponseEntity<DatosTopicoConRespuestas> registrarRespuesta(@PathVariable Long idTopico,
                           @RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                            UriComponentsBuilder uriComponentsBuilder){
        var topico = topicoRepository.getReferenceById(idTopico);
        var autor = usuarioRepository.getReferenceById(datosRegistroRespuesta.id_autor());
        var respuesta = respuestaRepository.save((new Respuesta(datosRegistroRespuesta, topico, autor)));
        var datosMostrarUsuario = new DatosMostrarUsuario(autor.getId(), autor.getNombre());
        var datosMostrarRespuesta = new DatosMostrarRespuesta(respuesta.getId(),respuesta.getMensaje(),respuesta.getFechaCreacion(),
                datosMostrarUsuario, respuesta.getSolucion());
        DatosTopicoConRespuestas datosTopicoConRespuestas = new DatosTopicoConRespuestas(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                datosMostrarUsuario, topico.getCurso(), List.of(datosMostrarRespuesta));
        URI url = uriComponentsBuilder.path("/{idTopico}/respuestas/{idRespuesta}").buildAndExpand(topico.getId().toString(),
                respuesta.getId().toString()).toUri();
        return ResponseEntity.created(url).body(datosTopicoConRespuestas);
    }
    @GetMapping
    @Operation(summary = "Mostrar todos los topicos")
    public ResponseEntity<List<DatosListadoTopico>> listarTopicos(){
        List<DatosListadoTopico> topicos = topicoRepository.findAll().stream().map(DatosListadoTopico::new).toList();
        return ResponseEntity.ok(topicos);
    }
    @GetMapping("/{idTopico}")
    @Operation(summary = "Mostrar un topico especifico")
    public ResponseEntity<DatosTopicoConRespuestas> retornarDatosTopico(@PathVariable Long idTopico) {
        var topico = topicoRepository.getReferenceById(idTopico);
        var respuestas = respuestaRepository.findByTopicoId(idTopico);

        List<DatosMostrarRespuesta> datosRespuestas = respuestas.stream()
                .map(respuesta -> new DatosMostrarRespuesta(respuesta.getId(), respuesta.getMensaje(),
                        respuesta.getFechaCreacion(), new DatosMostrarUsuario(respuesta.getAutor().getId(),
                        respuesta.getAutor().getNombre()), respuesta.getSolucion()))
                .collect(Collectors.toList());


        DatosTopicoConRespuestas datosTopicoConRespuestas = new DatosTopicoConRespuestas(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(topico.getAutor().getId(),topico.getAutor().getNombre()),
                topico.getCurso(), datosRespuestas);
        return ResponseEntity.ok(datosTopicoConRespuestas);
    }
    @PutMapping("/{idTopico}")
    @Operation(summary = "Actualizar un topico")
    @SecurityRequirement(name = "bearerAuth")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long idTopico,
                             @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        var topico = topicoRepository.getReferenceById(idTopico);
        topico.actualizarTopico(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(topico.getAutor().getId(),topico.getAutor().getNombre()),
                topico.getCurso()));
    }

    @PutMapping("/{idTopico}/respuestas/{idRespuesta}")
    @Operation(summary = "Actualizar una respuesta")
    @SecurityRequirement(name = "bearerAuth")
    @Transactional
    public ResponseEntity<DatosTopicoConRespuestaActualizada> actualizarRespuesta(@PathVariable Long idTopico,
                                              @PathVariable Long idRespuesta,
                                              @RequestBody @Valid DatosActualizarRespuesta datosActualizarRespuesta){
        var topico = topicoRepository.getReferenceById(idTopico);
        var respuesta = respuestaRepository.getReferenceById(idRespuesta);
        respuesta.actualizarTopico(datosActualizarRespuesta);
        DatosTopicoConRespuestaActualizada datosTopicoConRespuestaActualizada = new DatosTopicoConRespuestaActualizada(
                topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(topico.getAutor().getId(), topico.getAutor().getNombre()),topico.getCurso(),
                new DatosMostrarRespuesta(respuesta.getId(), respuesta.getMensaje(), respuesta.getFechaCreacion(),
                        new DatosMostrarUsuario(topico.getAutor().getId(), topico.getAutor().getNombre()),
                        respuesta.getSolucion()));
        return ResponseEntity.ok(datosTopicoConRespuestaActualizada);
    }

    @GetMapping("/{idTopico}/respuestas")
    @Operation(summary = "Mostrar las respuestas de un topico")
    public ResponseEntity<DatosTopicoConRespuestas> retornarTopicoConRespuestas(@PathVariable Long idTopico) {
        Topico topico = topicoRepository.getReferenceById(idTopico);
        var respuestas = respuestaRepository.findByTopicoId(idTopico);

        List<DatosMostrarRespuesta> datosRespuestas = respuestas.stream()
                .map(respuesta -> new DatosMostrarRespuesta(respuesta.getId(), respuesta.getMensaje(),
                        respuesta.getFechaCreacion(), new DatosMostrarUsuario(respuesta.getAutor().getId(),
                        respuesta.getAutor().getNombre()), respuesta.getSolucion()))
                .collect(Collectors.toList());
        DatosTopicoConRespuestas datosTopicoConRespuestas = new DatosTopicoConRespuestas(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(topico.getAutor().getId(),topico.getAutor().getNombre()),
                topico.getCurso(), datosRespuestas);
        return ResponseEntity.ok(datosTopicoConRespuestas);
    }

    @GetMapping("/{idTopico}/respuestas/{idRespuesta}")
    @Operation(summary = "Mostrar una respuesta especifica de un topico")
    public ResponseEntity<DatosTopicoConRespEspecifica> retornarDatosRespuesta(@PathVariable Long idTopico,
                                                                               @PathVariable Long idRespuesta) {
        Topico topico = topicoRepository.getReferenceById(idTopico);
        Respuesta respuesta = respuestaRepository.getReferenceById(idRespuesta);
        var datosMostrarRespuesta = new DatosMostrarRespuesta(respuesta.getId(),respuesta.getMensaje(),
                respuesta.getFechaCreacion(),new DatosMostrarUsuario(respuesta.getAutor().getId(),
                respuesta.getAutor().getNombre()),respuesta.getSolucion());
        var datosTopicoConRespEspecifica = new DatosTopicoConRespEspecifica(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(respuesta.getAutor().getId(), respuesta.getAutor().getNombre()),
                topico.getCurso(), List.of(datosMostrarRespuesta));
        return ResponseEntity.ok(datosTopicoConRespEspecifica);
    }
    @DeleteMapping("/{idTopico}")
    @Operation(summary = "Eliminar un topico")
    @SecurityRequirement(name = "bearerAuth")
    @Transactional
    public void eliminarTopico(@PathVariable Long idTopico){
        var topico = topicoRepository.getReferenceById(idTopico);
        topicoRepository.delete(topico);
    }
    @DeleteMapping("/{idTopico}/respuestas/{idRespuesta}")
    @Operation(summary = "Eliminar una respuesta")
    @SecurityRequirement(name = "bearerAuth")
    @Transactional
    public void eliminarRespuestaDeTopico(@PathVariable Long idTopico, @PathVariable Long idRespuesta){
        var topico = topicoRepository.getReferenceById(idTopico);
        var respuesta = respuestaRepository.getReferenceById(idRespuesta);
        respuestaRepository.delete(respuesta);
    }
}
