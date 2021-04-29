package com.github.gustavoflor.fronzabarberapi.core;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractPersistableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "ID_USER"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "ROLE")
    private Set<Role> roles;

    public boolean hasRole(Role role) {
        return getRoles().contains(role);
    }

}
