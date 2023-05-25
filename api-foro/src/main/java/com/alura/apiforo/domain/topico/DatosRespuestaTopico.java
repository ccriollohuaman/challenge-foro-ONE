package com.alura.apiforo.domain.topico;

import com.alura.apiforo.domain.curso.Curso;
import com.alura.apiforo.domain.usuario.DatosMostrarUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "Datos a mostrar del topico")
public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime fechaCreacion,
        StatusTopico status,
        DatosMostrarUsuario autor,
        Curso curso) {
}
