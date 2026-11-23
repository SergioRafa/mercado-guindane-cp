package com.claudiaguindanne.minimercado.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> campos = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "Valor invalido",
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validacao", campos.toString(), request.getRequestURI(), campos);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleBusinessRule(
            RuntimeException ex,
            HttpServletRequest request) {
        String mensagem = ex.getMessage() != null ? ex.getMessage() : "Requisicao invalida";
        return buildResponse(HttpStatus.BAD_REQUEST, "Regra de negocio", mensagem, request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String detalhe = "Parametro '" + ex.getName() + "' recebeu valor invalido: '" + ex.getValue() + "'";
        return buildResponse(HttpStatus.BAD_REQUEST, "Parametro invalido", detalhe, request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableMessage(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "JSON malformado",
                "Verifique a sintaxe ou os tipos dos campos enviados no corpo da requisicao",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Violacao de integridade",
                "Operacao nao permitida devido a restricao de dados ou vinculo existente no banco",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request) {
        String detalhe = ex.getMessage() != null ? ex.getMessage() : "Erro interno inesperado";
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                detalhe,
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String erro,
            String mensagem,
            String caminho,
            Map<String, String> detalhesCampos) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", erro);
        body.put("mensagem", mensagem);
        body.put("caminho", caminho);
        if (detalhesCampos != null && !detalhesCampos.isEmpty()) {
            body.put("campos", detalhesCampos);
        }
        return ResponseEntity.status(status).body(body);
    }
}
