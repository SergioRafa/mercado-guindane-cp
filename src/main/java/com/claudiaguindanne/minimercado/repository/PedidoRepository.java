package com.claudiaguindanne.minimercado.repository;

import com.claudiaguindanne.minimercado.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
