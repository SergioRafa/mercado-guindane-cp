package com.claudiaguindanne.minimercado.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping({ "/", "/api/status" })
    public ResponseEntity> status() {
        return ResponseEntity.ok(Map.of(
                "status", "ONLINE",
                "projeto", "Mercado Guindane CP",
                "mensagem", "API do Mercado Guindane CP rodando com sucesso!"
        ));
    }
}