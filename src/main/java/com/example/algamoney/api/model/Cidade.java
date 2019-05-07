package com.example.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "cidade")
public class Cidade {
    @Id
    Long codigo;

    String nome;

    @ManyToOne
    @JoinColumn(name = "codigo_estado")
    Estado estado;
}
