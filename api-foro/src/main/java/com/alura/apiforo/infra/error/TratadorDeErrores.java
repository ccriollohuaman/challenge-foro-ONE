package com.alura.apiforo.infra.error;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> tratarError404(EntityNotFoundException e){
        String mensaje = "No se encontró el elemento, valida la información";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
    }
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> tratarExcepcionValoresNulos(MethodArgumentNotValidException e) {
        String mensaje = e.getMessage();
        Map<String, String> campoMensajeMap = Map.of(
                "titulo", "El valor del campo 'titulo' no puede ser nulo.",
                "mensaje", "El valor del campo 'mensaje' no puede ser nulo.",
                "status", "El valor del campo 'status' no puede ser nulo.",
                "id_autor", "El valor del campo 'id_autor' no puede ser nulo.",
                "id_curso", "El valor del campo 'id_curso' no puede ser nulo."
        );
        for (Map.Entry<String, String> entry : campoMensajeMap.entrySet()) {
            String campo = entry.getKey();
            String mensajeError = entry.getValue();
            if (mensaje.contains("null") && mensaje.contains(campo)) {
                mensaje = mensajeError;
                break;
            }
        }
        return ResponseEntity.badRequest().body(mensaje);
    }
}
