package com.alura.apiforo.controller;

import com.alura.apiforo.domain.curso.CursoRepository;
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

    @Autowired
    public TopicoController(TopicoRepository topicoRepository, CursoRepository cursoRepository){
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
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
}
