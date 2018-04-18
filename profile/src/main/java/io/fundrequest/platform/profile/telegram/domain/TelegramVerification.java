package io.fundrequest.platform.profile.telegram.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "telegram_verifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramVerification {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "telegram_name")
    private String telegramName;
    @Column(name = "user_id")
    private String userId;
    private String secret;
    private boolean verified;
    @Column(name = "last_action")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAction;
}
