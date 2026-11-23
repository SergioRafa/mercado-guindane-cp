package com.claudiaguindanne.minimercado.controller;

import com.claudiaguindanne.minimercado.dto.VendaRequestDTO;
import com.claudiaguindanne.minimercado.dto.VendaResponseDTO;
import com.claudiaguindanne.minimercado.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<?> registrarVenda(@RequestBody @Valid VendaRequestDTO request) {
        try {
            VendaResponseDTO venda = vendaService.registrarVenda(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(venda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarVendas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return vendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarVenda(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vendaService.cancelarVenda(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
