package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersistentEntityDTO<I extends Serializable> {

    @NotNull
    private I id;

    public static <S extends Serializable> PersistentEntityDTO<S> of(S id) {
        return new PersistentEntityDTO<>(id);
    }

}
