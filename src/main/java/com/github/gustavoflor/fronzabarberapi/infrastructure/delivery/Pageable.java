package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

@Data
public class Pageable {

    @PositiveOrZero
    private Integer page = 0;

    @PositiveOrZero
    private Integer size = 8;

}
