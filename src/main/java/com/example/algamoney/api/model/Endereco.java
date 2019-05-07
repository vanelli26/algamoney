package com.example.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Embeddable
public class Endereco {

    String logradouro;

    String numero;

    String complemento;

    String bairro;

    String cep;

    @ManyToOne
    @JoinColumn(name = "codigo_cidade")
    Cidade cidade;
}
