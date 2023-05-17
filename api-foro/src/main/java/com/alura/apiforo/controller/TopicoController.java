package com.alura.apiforo.controller;

import com.alura.apiforo.domain.curso.CursoRepository;
import com.alura.apiforo.domain.topico.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico){
        var curso = cursoRepository.getReferenceById(datosRegistroTopico.id_curso());
        var topico = topicoRepository.save(new Topico(datosRegistroTopico, curso));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getStatus(), topico.getId_autor(), curso);
        return ResponseEntity.ok(datosRespuestaTopico);
    }
    @GetMapping
    public ResponseEntity<List<DatosListadoTopico>> listarTopicos(){
        List<DatosListadoTopico> topicos = topicoRepository.findAll().stream().map(DatosListadoTopico::new).toList();
        return ResponseEntity.ok(topicos);
    }
}
