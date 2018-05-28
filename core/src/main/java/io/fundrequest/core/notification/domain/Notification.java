package io.fundrequest.core.notification.domain;

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
import java.util.Objects;

@Table(name = "notification")
@Inheritance
@DiscriminatorColumn(name = "type")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;


    @Column(name = "blockchain_event_id")
    private Long blockchainEventId;

    @Column(name = "notification_date")
    private LocalDateTime date;

    protected Notification() {
    }

    public Notification(NotificationType type, LocalDateTime date, Long blockchainEventId) {
        this.type = type;
        this.date = date;
        this.blockchainEventId = blockchainEventId;
    }

    public Long getId() {
        return id;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getBlockchainEventId() {
        return blockchainEventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification that = (Notification) o;
        return type == that.type &&
               Objects.equals(date, that.date) &&
               Objects.equals(blockchainEventId, that.blockchainEventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, date, blockchainEventId);
    }
}
