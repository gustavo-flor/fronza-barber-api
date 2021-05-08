package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.PositiveOrZero;

@Data
public class Pageable {

    @PositiveOrZero
    private Integer page = 0;

    @PositiveOrZero
    private Integer size = 8;

    public PageRequest get() {
        return PageRequest.of(getPage(), getSize());
    }

}
