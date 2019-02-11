package com.example.algamoney.api.repository.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
public class LancamentoFilter {

    String descricao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dataVencimentoDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dataVencimentoAte;
}
