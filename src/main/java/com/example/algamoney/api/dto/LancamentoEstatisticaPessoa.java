package com.example.algamoney.api.dto;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.TipoLancamento;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class LancamentoEstatisticaPessoa {
    private TipoLancamento tipo;
    private Pessoa pessoa;
    private BigDecimal total;

    public LancamentoEstatisticaPessoa(TipoLancamento tipo, Pessoa pessoa, BigDecimal total) {
        this.tipo = tipo;
        this.pessoa = pessoa;
        this.total = total;
    }
}
