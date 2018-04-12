package io.fundrequest.platform.profile.telegram.repository;

import io.fundrequest.platform.profile.telegram.domain.TelegramVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TelegramVerificationRepository extends JpaRepository<TelegramVerification, Long> {

    Optional<TelegramVerification> findByTelegramNameAndSecret(@Param("telegramName") final String userId, final @Param("secret") String secret);

    List<TelegramVerification> findAllByTelegramName(@Param("telegramName") final String telegramName);

    Optional<TelegramVerification> findByUserId(@Param("userId") final String userId);
}
