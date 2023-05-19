package com.alura.apiforo.controller;

import com.alura.apiforo.domain.curso.CursoRepository;
import com.alura.apiforo.domain.respuesta.*;
import com.alura.apiforo.domain.topico.*;
import com.alura.apiforo.domain.usuario.DatosMostrarUsuario;
import com.alura.apiforo.domain.usuario.UsuarioRepository;
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
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder){
        var curso = cursoRepository.getReferenceById(datosRegistroTopico.id_curso());
        var autor = usuarioRepository.getReferenceById(datosRegistroTopico.id_autor());
        var topico = topicoRepository.save(new Topico(datosRegistroTopico, curso, autor));
        var datosMostrarUsuario = new DatosMostrarUsuario(autor.getId(), autor.getNombre());
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(), datosMostrarUsuario, curso);
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId().toString()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }
    @PostMapping("/{id}/respuestas")
    public ResponseEntity<DatosTopicoConRespuestas> registrarRespuesta(@PathVariable Long id,
                           @RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                            UriComponentsBuilder uriComponentsBuilder){
        var topico = topicoRepository.getReferenceById(id);
        var autor = usuarioRepository.getReferenceById(datosRegistroRespuesta.id_autor());
        var respuesta = respuestaRepository.save((new Respuesta(datosRegistroRespuesta, topico, autor)));
        var datosMostrarUsuario = new DatosMostrarUsuario(autor.getId(), autor.getNombre());
        var datosMostrarRespuesta = new DatosMostrarRespuesta(respuesta.getId(),respuesta.getMensaje(),respuesta.getFechaCreacion(),
                datosMostrarUsuario, respuesta.getSolucion());
        DatosTopicoConRespuestas datosTopicoConRespuestas = new DatosTopicoConRespuestas(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                datosMostrarUsuario, topico.getCurso(), List.of(datosMostrarRespuesta));
        URI url = uriComponentsBuilder.path("/{id}/respuestas/{idResp}").buildAndExpand(topico.getId().toString(),
                respuesta.getId().toString()).toUri();
        return ResponseEntity.created(url).body(datosTopicoConRespuestas);
    }
    @GetMapping
    public ResponseEntity<List<DatosListadoTopico>> listarTopicos(){
        List<DatosListadoTopico> topicos = topicoRepository.findAll().stream().map(DatosListadoTopico::new).toList();
        return ResponseEntity.ok(topicos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DatosTopicoConRespuestas> retornarDatosTopico(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        var respuestas = respuestaRepository.findByTopicoId(id);

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
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        var topico = topicoRepository.getReferenceById(id);
        topico.actualizarTopico(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(topico.getAutor().getId(),topico.getAutor().getNombre()),
                topico.getCurso()));
    }

    @PutMapping("/{id}/respuestas/{idResp}")
    @Transactional
    public ResponseEntity<DatosTopicoConRespuestaActualizada> actualizarRespuesta(@PathVariable Long id, @PathVariable Long idResp, @RequestBody
    @Valid DatosActualizarRespuesta datosActualizarRespuesta){
        var topico = topicoRepository.getReferenceById(id);
        var respuesta = respuestaRepository.getReferenceById(idResp);
        respuesta.actualizarTopico(datosActualizarRespuesta);
        DatosTopicoConRespuestaActualizada datosTopicoConRespuestaActualizada = new DatosTopicoConRespuestaActualizada(
                topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(topico.getAutor().getId(), topico.getAutor().getNombre()),topico.getCurso(),
                new DatosMostrarRespuesta(respuesta.getId(), respuesta.getMensaje(), respuesta.getFechaCreacion(),
                        new DatosMostrarUsuario(topico.getAutor().getId(), topico.getAutor().getNombre()),
                        respuesta.getSolucion()));
        return ResponseEntity.ok(datosTopicoConRespuestaActualizada);
    }

    @GetMapping("/{id}/respuestas")
    public ResponseEntity<DatosTopicoConRespuestas> retornarTopicoConRespuestas(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        var respuestas = respuestaRepository.findByTopicoId(id);

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

    @GetMapping("/{id}/respuestas/{idResp}")
    public ResponseEntity<DatosTopicoConRespEspecifica> retornarDatosRespuesta(@PathVariable Long id, @PathVariable Long idResp) {
        Topico topico = topicoRepository.getReferenceById(id);
        Respuesta respuesta = respuestaRepository.getReferenceById(idResp);
        var datosMostrarRespuesta = new DatosMostrarRespuesta(respuesta.getId(),respuesta.getMensaje(),
                respuesta.getFechaCreacion(),new DatosMostrarUsuario(respuesta.getAutor().getId(),
                respuesta.getAutor().getNombre()),respuesta.getSolucion());
        var datosTopicoConRespEspecifica = new DatosTopicoConRespEspecifica(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosMostrarUsuario(respuesta.getAutor().getId(), respuesta.getAutor().getNombre()),
                topico.getCurso(), List.of(datosMostrarRespuesta));
        return ResponseEntity.ok(datosTopicoConRespEspecifica);
    }
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarTopico(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);
        topicoRepository.delete(topico);
    }
    @DeleteMapping("/{id}/respuestas/{idResp}")
    @Transactional
    public void eliminarRespuestaDeTopico(@PathVariable Long id, @PathVariable Long idResp){
        var topico = topicoRepository.getReferenceById(id);
        var respuesta = respuestaRepository.getReferenceById(idResp);
        respuestaRepository.delete(respuesta);
    }
}
