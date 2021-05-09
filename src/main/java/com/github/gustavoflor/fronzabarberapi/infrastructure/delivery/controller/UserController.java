package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.controller;

import com.github.gustavoflor.fronzabarberapi.core.User;
import com.github.gustavoflor.fronzabarberapi.infrastructure.business.UserService;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.Pageable;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserCreateDTO;
import com.github.gustavoflor.fronzabarberapi.infrastructure.delivery.dto.UserShowDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(UserController.ENDPOINT)
@AllArgsConstructor
public class UserController {

    public static final String ENDPOINT = "/users";

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Transactional
    public ResponseEntity<UserShowDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User user = userService.insert(userCreateDTO);
        URI location = URI.create(ENDPOINT + "/" + user.getId());
        return ResponseEntity.created(location).body(UserShowDTO.of(user));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<UserShowDTO> show(@PathVariable Long id) {
        return userService.findById(id)
                .map(UserShowDTO::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<UserShowDTO>> index(@Valid Pageable pageable) {
        return ResponseEntity.ok(userService.paginate(pageable.get()).map(UserShowDTO::of));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
