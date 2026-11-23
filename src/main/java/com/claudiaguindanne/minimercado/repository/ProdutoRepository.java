package com.claudiaguindanne.minimercado.repository;

import com.claudiaguindanne.minimercado.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaIgnoreCase(String categoria);

    List<Produto> findByQuantidadeEstoqueLessThanEqual(Integer limite);
}