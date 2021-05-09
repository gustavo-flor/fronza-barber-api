package com.github.gustavoflor.fronzabarberapi.core;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractAuthenticableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "ROLE")
    private Set<Role> roles;

    public boolean hasRole(Role role) {
        return getRoles().contains(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(Role::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public enum Role {
        BARBER, MANAGER;

        private static final String AUTHORITY_PREFIX = "ROLE_";

        public String getAuthority() {
            return AUTHORITY_PREFIX + name();
        }
    }

    public boolean isBarber() {
        return hasRole(Role.BARBER);
    }

}
