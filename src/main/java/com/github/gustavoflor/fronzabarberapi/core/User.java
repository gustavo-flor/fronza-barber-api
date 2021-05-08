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

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "ROLE")
    private Set<Role> roles;

    public boolean hasRole(Role role) {
        return getRoles().contains(role);
    }

    public enum Role {
        BARBER, MANAGER
    }

    public boolean isBarber() {
        return hasRole(Role.BARBER);
    }

}
