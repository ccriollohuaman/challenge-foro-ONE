package com.alura.apiforo.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DatosMostrarRespuesta(
        Long id,
        String mensaje,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime fechaCreacion,
        String id_autor,
        Boolean solucion) {
}
