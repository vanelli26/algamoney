package com.example.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
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

    String descricao;

    @Column(name = "data_vencimento")
    LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    LocalDate dataPagamento;

    BigDecimal valor;

    String observacao;

    @Enumerated(EnumType.STRING)
    TipoLancamento tipo;

    @ManyToOne
    @JoinColumn(name = "codigo_categoria")
    Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "codigo_pessoa")
    Pessoa pessoa;
}
