package io.fundrequest.notification.domain;

import io.fundrequest.notification.dto.NotificationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "notification")
@Inheritance
@DiscriminatorColumn(name = "type")
@Entity
@Getter
@EqualsAndHashCode(exclude = {"id"})
public class Notification {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;


    @Column(name = "blockchain_event_id")
    private Long blockchainEventId;

    @Column(name = "notification_date")
    private LocalDateTime date;

    protected Notification() {
    }

    public Notification(final String uuid, final NotificationType type, final LocalDateTime date, final Long blockchainEventId) {
        this.uuid = uuid;
        this.type = type;
        this.date = date;
        this.blockchainEventId = blockchainEventId;
    }
}
