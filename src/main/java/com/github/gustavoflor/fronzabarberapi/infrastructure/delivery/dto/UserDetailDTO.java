package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ModelParser;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class UserDetailDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private Set<User.Authority> authorities;

    public static UserDetailDTO of(User user) {
        return ModelParser.transform(user, UserDetailDTO.class);
    }

}
