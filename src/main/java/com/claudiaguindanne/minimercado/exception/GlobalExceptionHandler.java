package com.claudiaguindanne.minimercado.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity> handleValidation(MethodArgumentNotValidException ex) {
        List> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(this::mapFieldError)
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de validacao nos dados enviados.",
                Map.of("errosCampos", campos)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity> handleDataIntegrity(DataIntegrityViolationException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Conflito de integridade de dados (verifique registros vinculados ou campos unicos).",
                null
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String msg = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();
        return buildResponse(status, msg, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado no servidor.",
                null
        );
    }

    private Map<String, String> mapFieldError(FieldError err) {
        Map<String, String> item = new LinkedHashMap<>();
        item.put("campo", err.getField());
        item.put("mensagem", err.getDefaultMessage() != null ? err.getDefaultMessage() : "Valor invalido");
        return item;
    }

    private ResponseEntity> buildResponse(
            HttpStatus status,
            String mensagem,
            Map<String, Object> detalhes
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("erro", status.getReasonPhrase());
        body.put("mensagem", mensagem);
        if (detalhes != null && !detalhes.isEmpty()) {
            body.put("detalhes", detalhes);
        }
        return ResponseEntity.status(status).body(body);
    }
}