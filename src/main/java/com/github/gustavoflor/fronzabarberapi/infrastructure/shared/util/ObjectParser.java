package com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class ObjectParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T transform(InputStream inputStream, Class<T> destinationType) throws IOException {
        if (inputStream == null) {
            return null;
        }
        return mapper.readValue(inputStream, destinationType);
    }

}
