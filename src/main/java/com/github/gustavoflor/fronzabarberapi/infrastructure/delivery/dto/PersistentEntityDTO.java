package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class PersistentEntityDTO<I extends Serializable> {

    @NotNull
    private I id;

}
