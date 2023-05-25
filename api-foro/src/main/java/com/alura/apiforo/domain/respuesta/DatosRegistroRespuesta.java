package com.alura.apiforo.domain.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Datos necesarios para registrar una nueva respuesta")
public record DatosRegistroRespuesta(
        @NotNull
        String mensaje,
        @NotNull
        Long id_autor) {
}
