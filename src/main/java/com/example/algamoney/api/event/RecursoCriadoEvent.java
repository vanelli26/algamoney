package com.example.algamoney.api.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import javax.servlet.http.HttpServletResponse;

@Getter
public class RecursoCriadoEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    HttpServletResponse response;
    Long codigo;

    public RecursoCriadoEvent(Object source, HttpServletResponse response, Long codigo) {
        super(source);
        this.response = response;
        this.codigo = codigo;
    }
}