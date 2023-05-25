package com.alura.apiforo.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "Datos necesarios para autenticar un usuario y generar el JWT")
public record DatosAutenticacionUsuario(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String contrasena) {
}
