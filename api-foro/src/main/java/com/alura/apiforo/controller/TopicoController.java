package com.alura.apiforo.controller;

import com.alura.apiforo.domain.curso.CursoRepository;
import com.alura.apiforo.domain.respuesta.DatosMostrarRespuesta;
import com.alura.apiforo.domain.respuesta.DatosRegistroRespuesta;
import com.alura.apiforo.domain.respuesta.Respuesta;
import com.alura.apiforo.domain.respuesta.RespuestaRepository;
import com.alura.apiforo.domain.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final CursoRepository cursoRepository;
    private final RespuestaRepository respuestaRepository;

    @Autowired
    public TopicoController(TopicoRepository topicoRepository, CursoRepository cursoRepository,
                            RespuestaRepository respuestaRepository){
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
        this.respuestaRepository = respuestaRepository;
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder){
        var curso = cursoRepository.getReferenceById(datosRegistroTopico.id_curso());
        var topico = topicoRepository.save(new Topico(datosRegistroTopico, curso));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getStatus(), topico.getId_autor(), curso);
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId().toString()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }
    @PostMapping("/{id}/respuestas")
    public ResponseEntity<DatosTopicoConRespuestas> registrarRespuesta(@PathVariable Long id,
                           @RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                            UriComponentsBuilder uriComponentsBuilder){
        var topico = topicoRepository.getReferenceById(id);
        var respuesta = respuestaRepository.save((new Respuesta(datosRegistroRespuesta, topico)));
        DatosTopicoConRespuestas datosTopicoConRespuestas = new DatosTopicoConRespuestas(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getStatus(), topico.getId_autor(), topico.getCurso(), topico.getRespuestas());
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
    public ResponseEntity<DatosRespuestaTopico> retornarDatosTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(),
                topico.getStatus(), topico.getId_autor(), topico.getCurso());
        return ResponseEntity.ok(datosTopico);
    }
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizarTopico(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(),
                topico.getStatus(), topico.getId_autor(), topico.getCurso()));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarTopico(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);
        topicoRepository.delete(topico);
    }
    @GetMapping("/{id}/respuestas")
    public ResponseEntity<DatosTopicoConRespuestas> retornarTopicoConRespuestas(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        DatosTopicoConRespuestas datosTopicoConRespuestas = new DatosTopicoConRespuestas(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getStatus(), topico.getId_autor(), topico.getCurso(), topico.getRespuestas());
        return ResponseEntity.ok(datosTopicoConRespuestas);
    }

    @GetMapping("/{id}/respuestas/{idResp}")
    public ResponseEntity<DatosTopicoConRespEspecifica> retornarDatosRespuesta(@PathVariable Long id, @PathVariable Long idResp) {
        Topico topico = topicoRepository.getReferenceById(id);
        Respuesta respuesta = respuestaRepository.getReferenceById(idResp);
        var datosMostrarRespuesta = new DatosMostrarRespuesta(respuesta.getId(),respuesta.getMensaje(),
                respuesta.getFechaCreacion(),respuesta.getId_autor(),respuesta.getSolucion());
        var datosTopicoConRespEspecifica = new DatosTopicoConRespEspecifica(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(), topico.getId_autor(),
                topico.getCurso(), List.of(datosMostrarRespuesta));
        return ResponseEntity.ok(datosTopicoConRespEspecifica);
    }

    @DeleteMapping("/{id}/respuestas/{idResp}")
    @Transactional
    public void eliminarRespuestaDeTopico(@PathVariable Long id, @PathVariable Long idResp){
        var topico = topicoRepository.getReferenceById(id);
        var respuesta = respuestaRepository.getReferenceById(idResp);
        respuestaRepository.delete(respuesta);
    }
}
