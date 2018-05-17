package io.fundrequest.platform.profile.profile;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.keycloak.UserIdentity;
import io.fundrequest.platform.profile.developer.verification.event.DeveloperVerified;
import io.fundrequest.platform.profile.profile.dto.UserLinkedProviderEvent;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import io.fundrequest.platform.profile.profile.dto.UserProfileProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.common.util.Base64Url;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final KeycloakRepository keycloakRepository;
    private final String keycloakUrl;
    private String intercomHmacKey;
    private final ApplicationEventPublisher eventPublisher;

    public ProfileServiceImpl(final KeycloakRepository keycloakRepository,
                              final @Value("${keycloak.auth-server-url}") String keycloakUrl,
                              final @Value("${io.fundrequest.intercom.secret}") String intercomHmacKey,
                              final ApplicationEventPublisher eventPublisher) {
        this.keycloakRepository = keycloakRepository;
        this.keycloakUrl = keycloakUrl;
        this.intercomHmacKey = intercomHmacKey;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @CacheEvict(value = "user_profile", key = "#principal.name", beforeInvocation = true)
    public void userProviderIdentityLinked(Principal principal, Provider provider) {
        eventPublisher.publishEvent(UserLinkedProviderEvent.builder().principal(principal).provider(provider).build());
    }

    @Override
    @Cacheable(value = "user_profile", key = "#userId")
    public UserProfile getUserProfile(String userId) {
        Map<Provider, UserProfileProvider> providers = keycloakRepository.getUserIdentities(userId)
                                                                         .collect(Collectors.toMap(UserIdentity::getProvider,
                                                                                                   x -> UserProfileProvider.builder()
                                                                                                                           .userId(x.getUserId())
                                                                                                                           .username(x.getUsername())
                                                                                                                           .build()));
        UserRepresentation user = keycloakRepository.getUser(userId);
        return UserProfile.builder()
                          .id(user.getId())
                          .name(user.getFirstName() + " " + user.getLastName())
                          .createdAt(user.getCreatedTimestamp())
                          .email(user.getEmail())
                          .picture(getPicture(user))
                          .verifiedDeveloper(keycloakRepository.isVerifiedDeveloper(user))
                          .etherAddress(keycloakRepository.getEtherAddress(user))
                          .telegramName(keycloakRepository.getTelegramName(user))
                          .headline(keycloakRepository.getHeadline(user))
                          .github(providers.get(Provider.GITHUB))
                          .linkedin(providers.get(Provider.LINKEDIN))
                          .twitter(providers.get(Provider.TWITTER))
                          .google(providers.get(Provider.GOOGLE))
                          .stackoverflow(providers.get(Provider.STACKOVERFLOW))
                          .emailSignedVerification(getEmailSignedVerification(user.getEmail()))
                          .build();
    }

    private String getEmailSignedVerification(String email) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(intercomHmacKey.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(email.getBytes("UTF-8")));

        } catch (Exception e) {
            log.error("Error creating hmac verified email", e);
            return null;
        }
    }

    @Override
    @Cacheable(value = "user_profile", key = "#principal.name")
    public UserProfile getUserProfile(Principal principal) {
        return getUserProfile(principal.getName());
    }

    @EventListener
    @CacheEvict(value = "user_profile", key = "#event.userId")
    public void onDeveloperVerified(DeveloperVerified event) {
        keycloakRepository.updateVerifiedDeveloper(event.getUserId(), true);
    }

    private String getPicture(UserRepresentation ur) {
        String picture = keycloakRepository.getPicture(ur);
        if (StringUtils.isNotBlank(picture) && picture.endsWith("?sz=50")) {
            picture += "0";
        }
        return picture;
    }

    @Override
    @CacheEvict(value = "user_profile", key = "#principal.name")
    public void updateEtherAddress(Principal principal, String etherAddress) {
        keycloakRepository.updateEtherAddress(principal.getName(), etherAddress);
    }

    @Override
    @CacheEvict(value = "user_profile", key = "#principal.name")
    public void updateTelegramName(Principal principal, String telegramName) {
        keycloakRepository.updateTelegramName(principal.getName(), telegramName);
    }

    @Override
    @CacheEvict(value = "user_profile", key = "#principal.name")
    public void updateHeadline(Principal principal, String headline) {
        keycloakRepository.updateHeadline(principal.getName(), headline);
    }

    @Override
    @CacheEvict(value = "user_profile", key = "#principal.name")
    public void logout(Principal principal) {
        log.info("User " + principal.getName() + " has logged out");
    }

    @Override
    public String createSignupLink(HttpServletRequest request, Principal principal, Provider providerEnum) {
        String provider = providerEnum.name().toLowerCase();
        AccessToken token = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken();
        String clientId = token.getIssuedFor();
        String nonce = UUID.randomUUID().toString();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String input = nonce + token.getSessionState() + clientId + provider;
        byte[] check = md.digest(input.getBytes(StandardCharsets.UTF_8));
        String hash = Base64Url.encode(check);
        request.getSession().setAttribute("hash", hash);
        return KeycloakUriBuilder.fromUri(keycloakUrl)
                                 .path("/realms/{realm}/broker/{provider}/link")
                                 .queryParam("nonce", nonce)
                                 .queryParam("hash", hash)
                                 .queryParam("client_id", clientId)
                                 .queryParam("redirect_uri", getRedirectUrl(request, provider)).build("fundrequest", provider).toString();
    }

    private String getRedirectUrl(HttpServletRequest req, String provider) {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        String url = scheme + "://" + serverName;

        if (serverPort != 80 && serverPort != 443) {
            url += ":" + serverPort;
        }
        url += contextPath;
        if (!url.endsWith("/")) {
            url += "/";
        }
        url += "profile/link/" + provider + "/redirect";
        return url;
    }
}
