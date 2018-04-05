package io.fundrequest.core.token.infrastructure;


import io.fundrequest.core.token.domain.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenInfoRepository extends JpaRepository<TokenInfo, String> {
}
