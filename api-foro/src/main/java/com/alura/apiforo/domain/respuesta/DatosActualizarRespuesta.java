package com.alura.apiforo.domain.respuesta;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarRespuesta(@NotNull String mensaje, @NotNull Boolean solucion) {
}
