package io.fundrequest.platform.profile.profile;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileServiceImplTest {

    private ProfileServiceImpl profileService;
    private KeycloakRepository keycloakRepository;

    @Before
    public void setUp() throws Exception {
        keycloakRepository = mock(KeycloakRepository.class);
        profileService = new ProfileServiceImpl(keycloakRepository, "url", "secret", mock(ApplicationEventPublisher.class));
    }

    @Test
    public void getUserProfileReturnsCreationDate() {
        UserRepresentation userRepresentation = new UserRepresentation();
        Date creationDate = DateUtil.parse("2018-01-31");
        userRepresentation.setCreatedTimestamp(creationDate.getTime());
        userRepresentation.setEmail("davy.van.roy@gmail.com");
        when(keycloakRepository.getUser("davy")).thenReturn(userRepresentation);
        when(keycloakRepository.getUserIdentities("davy")).thenReturn(Stream.empty());

        UserProfile userProfile = profileService.getUserProfile("davy");

        assertThat(userProfile.getCreatedAt()).isEqualTo(creationDate.getTime());
    }

    @Test
    public void getUserProfileReturnsEmailVerifiedSignature() throws Exception {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail("davy.van.roy@gmail.com");
        when(keycloakRepository.getUser("davy")).thenReturn(userRepresentation);
        when(keycloakRepository.getUserIdentities("davy")).thenReturn(Stream.empty());

        UserProfile userProfile = profileService.getUserProfile("davy");

        assertThat(userProfile.getEmailSignedVerification()).isEqualTo("424a4f5feb0d4adeec05c717f6260c734a0ca036a133c7d29e911ac3c4fbb775");
    }
}