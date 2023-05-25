package com.alura.apiforo.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Schema(description = "Datos necesarios para registrar un nuevo usuario")
public record DatosRegistroUsuario(

        @NotBlank
        String nombre,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String contrasena) {
}
