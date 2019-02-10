package com.example.algamoney.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long codigo;

    @NotNull
    @Size(min = 1, max = 50)
    String nome;

    @Embedded
    Endereco endereco;

    @NotNull
    Boolean ativo;

    @JsonIgnore
    @Transient
    public boolean isInativo() {
        return !this.ativo;
    }
}