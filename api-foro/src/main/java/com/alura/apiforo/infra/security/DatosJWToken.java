package com.alura.apiforo.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token devuelto al realizar la autenticación de usuario")
public record DatosJWToken(String jwtoken) {
}
