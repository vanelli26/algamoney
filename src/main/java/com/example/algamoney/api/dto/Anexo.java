package com.example.algamoney.api.dto;

import lombok.Getter;

@Getter
public class Anexo {
    private String nome;
    private String url;

    public Anexo(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }
}
