package io.fundrequest.platform.profile.telegram.service;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.event.CreateBountyCommand;
import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.telegram.domain.TelegramVerification;
import io.fundrequest.platform.profile.telegram.repository.TelegramVerificationRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
public class TelegramVerificationService {

    private final TelegramVerificationRepository telegramVerificationRepository;
    private final BountyService bountyService;
    private KeycloakRepository keycloakRepository;

    public TelegramVerificationService(final TelegramVerificationRepository telegramVerificationRepository,
                                       final BountyService bountyService,
                                       final KeycloakRepository keycloakRepository) {
        this.telegramVerificationRepository = telegramVerificationRepository;
        this.bountyService = bountyService;
        this.keycloakRepository = keycloakRepository;
    }

    @Transactional(readOnly = true)
    public boolean exists(final String telegramName) {
        return !telegramVerificationRepository.findAllByTelegramName(telegramName).isEmpty();
    }

    @Transactional(readOnly = true)
    public boolean existsForUserId(final String userId) {
        return telegramVerificationRepository.findByUserId(userId).isPresent();
    }

    public Optional<TelegramVerification> getByUserId(final Principal principal) {
        return telegramVerificationRepository.findByUserId(principal.getName());
    }

    @Transactional
    public boolean verify(final String telegramName, final String secret) {
        final Optional<TelegramVerification> byUserIdAndSecret = telegramVerificationRepository.findByTelegramNameAndSecret(telegramName, secret);
        if (byUserIdAndSecret.isPresent()) {
            final TelegramVerification telegramVerification = byUserIdAndSecret.get();
            if (!telegramVerification.isVerified()) {
                bountyService.createBounty(
                        CreateBountyCommand.builder()
                                           .type(BountyType.LINK_TELEGRAM)
                                           .userId(telegramVerification.getUserId())
                                           .build()
                                          );
            }
            telegramVerification.setLastAction(new Date());
            telegramVerification.setVerified(true);
            telegramVerificationRepository.save(telegramVerification);
            return true;
        } else {
            return false;
        }
    }

    public void createTelegramVerification(final String userId, final String telegramname) {
        if (keycloakRepository.isVerifiedDeveloper(userId)) {
            final Optional<TelegramVerification> byUserId = telegramVerificationRepository.findByUserId(userId);
            if (byUserId.isPresent()) {
                TelegramVerification telegramVerification = byUserId.get();
                if (telegramVerification.isVerified()) {
                    throw new IllegalArgumentException("Principal has already verified a telegram");
                } else {
                    telegramVerification.setTelegramName(telegramname);
                    telegramVerification.setUserId(userId);
                    telegramVerification.setLastAction(new Date());
                    telegramVerification.setSecret(createSecret(userId));
                    telegramVerificationRepository.save(
                            telegramVerification
                                                       );
                }
            } else {
                telegramVerificationRepository.save(
                        new TelegramVerification(null, telegramname, userId, createSecret(userId), false, new Date())
                                                   );
            }
        }
    }

    public String createSecret(final String userId) {
        return DigestUtils.sha1Hex(userId);
    }
}
