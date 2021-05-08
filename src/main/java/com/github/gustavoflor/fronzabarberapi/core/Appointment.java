package com.github.gustavoflor.fronzabarberapi.core;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "STATUS", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_USER_ID")
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BARBER_USER_ID")
    private User barber;

    @Getter
    @AllArgsConstructor
    public enum Status {
        PENDING(true, true, true),
        ACCEPTED(true, false, false),
        REFUSED(false, false, false),
        CANCELED(false, false, false);

        private final boolean cancelable;
        private final boolean acceptable;
        private final boolean refusable;
    }

    public boolean canCancel() {
        return getStatus().isCancelable();
    }

    public boolean canAccept() {
        return getStatus().isAcceptable();
    }

    public boolean canRefuse() {
        return getStatus().isRefusable();
    }

    public void setPending() {
        setStatus(Status.PENDING);
    }

    public void setAccepted() {
        setStatus(Status.ACCEPTED);
    }

    public void setRefused() {
        setStatus(Status.REFUSED);
    }

    public void setCanceled() {
        setStatus(Status.CANCELED);
    }

}
