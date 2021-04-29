package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.controller;

import com.github.gustavoflor.fronzabarberapi.core.Role;
import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.UserService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.Pageable;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.Restrict;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<UserDetailDTO> store(@Valid @RequestBody UserCreateDTO createDTO) {
        User user = userService.insert(createDTO.transform());
        URI location = URI.create(String.format("/users/%s", user.getId()));
        return ResponseEntity.created(location).body(UserDetailDTO.of(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> show(@PathVariable Long id) {
        return userService.findById(id)
                .map(UserDetailDTO::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Restrict(to = Role.MANAGER)
    @GetMapping
    public ResponseEntity<Page<UserDetailDTO>> paginate(Pageable pageable) {
        return ResponseEntity.ok(userService.paginate(pageable.getPage(), pageable.getSize()).map(UserDetailDTO::of));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
