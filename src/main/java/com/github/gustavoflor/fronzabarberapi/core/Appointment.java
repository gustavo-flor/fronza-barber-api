package com.github.gustavoflor.fronzabarberapi.core;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "APPOINTMENTS")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends AbstractPersistableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATUS")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID_USER")
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BARBER_ID_USER")
    private User barber;

    public enum Status {
        PENDING, ACCEPTED, REFUSED
    }

}
