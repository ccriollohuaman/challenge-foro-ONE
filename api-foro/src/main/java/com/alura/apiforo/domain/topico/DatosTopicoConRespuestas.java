package com.alura.apiforo.domain.topico;

import com.alura.apiforo.domain.curso.Curso;
import com.alura.apiforo.domain.respuesta.Respuesta;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record DatosTopicoConRespuestas(
        Long id,
        String titulo,
        String mensaje,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String id_autor,
        Curso curso,
        List<Respuesta> respuestas) {
}
