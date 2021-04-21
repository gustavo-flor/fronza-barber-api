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

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_AUTHORITIES", joinColumns = @JoinColumn(name = "ID_USER"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "AUTHORITY")
    private Set<Authority> authorities;

    public enum Authority {
        BARBER, MANAGER
    }

    public boolean hasAuthority(Authority authority) {
        return getAuthorities().contains(authority);
    }

}
