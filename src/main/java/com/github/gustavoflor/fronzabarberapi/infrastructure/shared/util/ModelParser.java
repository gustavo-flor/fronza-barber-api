package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class ModelParser {

    private final ModelMapper mapper = new ModelMapper();

    public <T> T transform(Object object, Class<T> destinationType) {
        if (object == null) {
            return null;
        }
        return mapper.map(object, destinationType);
    }

}
