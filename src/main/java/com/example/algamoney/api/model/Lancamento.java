package com.example.algamoney.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lancamento")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long codigo;

    @NotNull
    String descricao;

    @NotNull
    @Column(name = "data_vencimento")
    LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    LocalDate dataPagamento;

    @NotNull
    BigDecimal valor;

    String observacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    TipoLancamento tipo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_categoria")
    Categoria categoria;

    @JsonIgnoreProperties("contatos")
    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_pessoa")
    Pessoa pessoa;

    @JsonIgnore
    public boolean isReceita() { return TipoLancamento.RECEITA.equals(this.tipo); }
}
