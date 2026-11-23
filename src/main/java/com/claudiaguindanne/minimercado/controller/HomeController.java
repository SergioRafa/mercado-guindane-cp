package com.claudiaguindanne.minimercado.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Map.of(
                "status", "ONLINE",
                "projeto", "Mercado Guindane CP",
                "mensagem", "API do Mercado Guindane CP rodando com sucesso!"
        ));
    }
}
