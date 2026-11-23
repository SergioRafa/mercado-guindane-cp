package com.claudiaguindanne.minimercado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do produto e obrigatorio")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Categoria e obrigatoria")
    @Column(nullable = false)
    private String categoria;

    @NotNull(message = "Preco de custo e obrigatorio")
    @PositiveOrZero(message = "Preco de custo nao pode ser negativo")
    @Column(name = "preco_custo", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoCusto;

    @NotNull(message = "Preco de venda e obrigatorio")
    @PositiveOrZero(message = "Preco de venda nao pode ser negativo")
    @Column(name = "preco_venda", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoVenda;

    @NotNull(message = "Quantidade em estoque e obrigatoria")
    @PositiveOrZero(message = "Quantidade em estoque nao pode ser negativa")
    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    public Produto() {
    }

    public Produto(String nome, String categoria, BigDecimal precoCusto, BigDecimal precoVenda, Integer quantidadeEstoque) {
        this.nome = nome;
        this.categoria = categoria;
        this.precoCusto = precoCusto != null ? precoCusto.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.precoVenda = precoVenda != null ? precoVenda.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public BigDecimal getMargemLucroPercentual() {
        if (precoVenda == null || precoCusto == null || precoVenda.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return precoVenda.subtract(precoCusto)
                .divide(precoVenda, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValorTotalEstoque() {
        if (precoVenda == null || quantidadeEstoque == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return precoVenda.multiply(BigDecimal.valueOf(quantidadeEstoque)).setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto != null ? precoCusto.setScale(2, RoundingMode.HALF_UP) : null;
    }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda != null ? precoVenda.setScale(2, RoundingMode.HALF_UP) : null;
    }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
}
