package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.shared.util.ModelParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserShowDTO {

    @Positive
    private Long id;

    @NotBlank
    private String name;

    private Set<User.Role> roles;

    public static UserShowDTO of(User user) {
        return ModelParser.transform(user, UserShowDTO.class);
    }

}
