package io.fundrequest.core.token.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "token_info")
@Entity
@Getter
public class TokenInfo {
    @Id
    @Column(name = "address")
    private String address;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "decimals")
    private Long decimals;

    TokenInfo() {
    }

    @Builder
    public TokenInfo(String address, String symbol, String name, Long decimals) {
        this.address = address;
        this.symbol = symbol;
        this.name = name;
        this.decimals = decimals;
    }
}
