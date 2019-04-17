package com.example.algamoney.api.model;

import lombok.Getter;

@Getter
public enum TipoLancamento {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private final String descricao;

    TipoLancamento(String descricao) {
        this.descricao = descricao;
    }
}
