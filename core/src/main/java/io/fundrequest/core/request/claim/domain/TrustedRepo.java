package io.fundrequest.core.request.claim.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "trusted_repo")
@NoArgsConstructor
@Getter
public class TrustedRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "owner")
    private String owner;

    @Builder
    public TrustedRepo(String owner) {
        this.owner = owner;
    }
}
