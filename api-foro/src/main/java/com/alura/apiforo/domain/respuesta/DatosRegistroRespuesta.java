package com.alura.apiforo.domain.respuesta;

import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
        @NotNull
        String mensaje,
        @NotNull
        Long id_autor) {
}
