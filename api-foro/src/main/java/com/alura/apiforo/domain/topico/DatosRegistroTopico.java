package com.alura.apiforo.domain.topico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Datos necesarios para registrar un tópico")
public record DatosRegistroTopico(
        @NotBlank
        String titulo,
        @NotBlank
        String mensaje,
        @NotNull
        Long id_autor,
        @NotNull
        Long id_curso) {
}
