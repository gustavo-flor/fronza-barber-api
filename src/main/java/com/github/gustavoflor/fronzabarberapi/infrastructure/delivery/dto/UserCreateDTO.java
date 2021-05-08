package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ModelParser;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
public class UserCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    private Set<User.Role> roles;

    public User transform() {
        return ModelParser.transform(this, User.class);
    }

}
