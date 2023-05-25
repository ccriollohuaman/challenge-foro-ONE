package com.alura.apiforo.domain.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos a mostrar del usuario")
public record DatosMostrarUsuario(Long id, String nombre) {
}
