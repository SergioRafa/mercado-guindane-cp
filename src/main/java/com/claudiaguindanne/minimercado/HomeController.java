package com.claudiaguindanne.minimercado;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> status() {
        return Map.of(
            "aplicacao", "minimercado-v2",
            "status", "online",
            "endpoints", new String[] { "/produtos", "/produtos/financeiro/resumo", "/vendas" }
        );
    }
}