package io.fundrequest.platform.profile.profile;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.keycloak.UserIdentity;
import io.fundrequest.platform.profile.arkane.ArkaneRepository;
import io.fundrequest.platform.profile.arkane.Wallet;
import io.fundrequest.platform.profile.arkane.WalletMother;
import io.fundrequest.platform.profile.arkane.WalletsResult;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileServiceImplTest {

    private ProfileServiceImpl profileService;
    private KeycloakRepository keycloakRepository;
    private ArkaneRepository arkaneRepository;

    @BeforeEach
    public void setUp() throws Exception {
        keycloakRepository = mock(KeycloakRepository.class);
        arkaneRepository = mock(ArkaneRepository.class);
        profileService = new ProfileServiceImpl(keycloakRepository, "url", "fundrequest", "secret", mock(ApplicationEventPublisher.class), arkaneRepository);
    }

    @Test
    public void getUserProfileReturnsCreationDate() {
        UserRepresentation userRepresentation = new UserRepresentation();
        Date creationDate = DateUtil.parse("2018-01-31");
        userRepresentation.setCreatedTimestamp(creationDate.getTime());
        userRepresentation.setEmail("davy.van.roy@gmail.com");
        when(keycloakRepository.getUser("davy")).thenReturn(userRepresentation);
        when(keycloakRepository.getUserIdentities("davy")).thenReturn(Stream.empty());

        UserProfile userProfile = profileService.getUserProfile(() -> "davy");

        assertThat(userProfile.getCreatedAt()).isEqualTo(creationDate.getTime());
    }

    @Test
    public void getUserProfileReturnsEmailVerifiedSignature() throws Exception {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail("davy.van.roy@gmail.com");
        when(keycloakRepository.getUser("davy")).thenReturn(userRepresentation);
        when(keycloakRepository.getUserIdentities("davy")).thenReturn(Stream.empty());

        UserProfile userProfile = profileService.getUserProfile(() -> "davy");

        assertThat(userProfile.getEmailSignedVerification()).isEqualTo("424a4f5feb0d4adeec05c717f6260c734a0ca036a133c7d29e911ac3c4fbb775");
    }

    @Test
    void getProfileWithWallets() {
        KeycloakAuthenticationToken principal = new KeycloakAuthenticationToken(new KeycloakAccount() {
            @Override
            public Principal getPrincipal() {
                return () -> "davy";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }
        }, true);
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail("davy.van.roy@arkane.network");
        when(keycloakRepository.getUser("davy")).thenReturn(userRepresentation);
        when(keycloakRepository.getAccessToken(principal, Provider.ARKANE)).thenReturn("token");
        Wallet expectedWallet = WalletMother.aWallet();
        when(keycloakRepository.getUserIdentities("davy")).thenReturn(Stream.of(UserIdentity.builder().provider(Provider.ARKANE).build()));
        WalletsResult walletsResult = new WalletsResult();
        walletsResult.setResult(Collections.singletonList(expectedWallet));
        when(arkaneRepository.getWallets("Bearer token")).thenReturn(walletsResult);

        profileService.getUserProfile(principal);

    }
}
