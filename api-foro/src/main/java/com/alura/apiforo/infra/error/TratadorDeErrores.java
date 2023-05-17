package com.alura.apiforo.infra.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> tratarExcepcionValoresDuplicados(SQLIntegrityConstraintViolationException e){
        String mensaje = e.getMessage();
        if (mensaje.contains("Duplicate entry") && mensaje.contains("'topicos.uk_titulo'")) {
            mensaje = "El valor del campo 'titulo' ya existe en la base de datos.";
        } else if (mensaje.contains("Duplicate entry") && mensaje.contains("'topicos.uk_mensaje'")) {
            mensaje = "El valor del campo 'mensaje' ya existe en la base de datos.";
        }
        return ResponseEntity.badRequest().body(mensaje);
    }

}
