package com.alura.apiforo.domain.topico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Datos necesarios para actualizar un tópico")
public record DatosActualizarTopico(
        @NotNull
        String titulo,
        @NotNull
        String mensaje,
        @NotNull
        StatusTopico status) {
}
