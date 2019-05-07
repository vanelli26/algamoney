package com.example.algamoney.api.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "estado")
public class Estado {
    @Id
    Long codigo;

    String nome;
}
