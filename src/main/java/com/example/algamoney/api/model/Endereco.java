package com.example.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Endereco {

    String logradouro;
    String numero;
    String complemento;
    String bairro;
    String cep;
    String cidade;
    String estado;
}
