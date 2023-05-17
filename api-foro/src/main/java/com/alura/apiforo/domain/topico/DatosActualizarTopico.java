package com.alura.apiforo.domain.topico;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(
        @NotNull
        String titulo,
        @NotNull
        String mensaje,
        @NotNull
        StatusTopico status) {
}
