package io.fundrequest.platform.profile.survey.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "survey")
@Getter
public class Survey {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "ether_address")
    private String etherAddress;


    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private SurveyStatus status;

    Survey() {
    }

    @Builder
    Survey(String userId, String email, String etherAddress, SurveyStatus status) {
        this.userId = userId;
        this.email = email;
        this.etherAddress = etherAddress;
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Survey survey = (Survey) o;
        return Objects.equals(email, survey.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email);
    }
}
