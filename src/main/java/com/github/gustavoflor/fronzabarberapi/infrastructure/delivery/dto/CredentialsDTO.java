package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public UsernamePasswordAuthenticationToken getAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(getEmail(), getPassword(), new HashSet<>());
    }

}
