package com.alura.apiforo.domain.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Datos necesarios para actualizar una respuesta")
public record DatosActualizarRespuesta(@NotNull String mensaje, @NotNull Boolean solucion) {
}
