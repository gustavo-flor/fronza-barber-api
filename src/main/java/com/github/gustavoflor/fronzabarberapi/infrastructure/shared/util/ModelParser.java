package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import org.modelmapper.ModelMapper;

import java.util.Optional;

public class ModelParser {

    private ModelParser() {
    }

    private static final ModelMapper mapper = new ModelMapper();

    public static <T> T transform(Object object, Class<T> destinationType) {
        return Optional.ofNullable(object)
                .map(source -> mapper.map(source, destinationType))
                .orElse(null);
    }

}
