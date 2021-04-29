package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.Role;
import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ModelParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private Set<Role> authorities;

    public static UserDetailDTO of(User user) {
        return ModelParser.transform(user, UserDetailDTO.class);
    }

}
