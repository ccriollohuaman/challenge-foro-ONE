package com.alura.apiforo.domain.topico;

import com.alura.apiforo.domain.curso.Curso;
import com.alura.apiforo.domain.respuesta.DatosMostrarRespuesta;
import com.alura.apiforo.domain.usuario.DatosMostrarUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "Datos a mostrar de un topico con respuesta específica")
public record DatosTopicoConRespEspecifica(
        Long id,
        String titulo,
        String mensaje,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime fechaCreacion,
        StatusTopico status,
        DatosMostrarUsuario autor,
        Curso curso,
        List<DatosMostrarRespuesta> respuestas) {
}
