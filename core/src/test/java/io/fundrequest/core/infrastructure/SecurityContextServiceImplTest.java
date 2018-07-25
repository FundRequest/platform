package io.fundrequest.core.infrastructure;

import io.fundrequest.common.infrastructure.SecurityContextHolderSpringDelegate;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class SecurityContextServiceImplTest {


    private SecurityContextService securityContextService;
    private SecurityContextHolderSpringDelegate securityContextHolder;
    private ProfileService profileService;

    @BeforeEach
    public void setUp() {
        securityContextHolder = mock(SecurityContextHolderSpringDelegate.class, RETURNS_DEEP_STUBS);
        profileService = mock(ProfileService.class);
        securityContextService = new SecurityContextServiceImpl(securityContextHolder, profileService);
    }

    @Test
    public void getLoggedInUser_present() {
        final Authentication expected = mock(Authentication.class);

        when(securityContextHolder.getContext().getAuthentication()).thenReturn(expected);

        final Optional<Authentication> loggedInUser = securityContextService.getLoggedInUser();

        assertThat(loggedInUser).containsSame(expected);
    }

    @Test
    public void getLoggedInUser_notPresent() {
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(null);

        final Optional<Authentication> loggedInUser = securityContextService.getLoggedInUser();

        assertThat(loggedInUser).isEmpty();
    }

    @Test
    public void isUserFullyAuthenticated() {
        final Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final boolean result = securityContextService.isUserFullyAuthenticated();

        assertThat(result).isTrue();
    }

    @Test
    public void isUserFullyAuthenticated_noAuthentication() {
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(null);

        final boolean result = securityContextService.isUserFullyAuthenticated();

        assertThat(result).isFalse();
    }

    @Test
    public void isUserFullyAuthenticated_notAuthentciated() {
        final Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(false);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final boolean result = securityContextService.isUserFullyAuthenticated();

        assertThat(result).isFalse();
    }

    @Test
    public void isUserFullyAuthenticated_AnonymousAuthenticationToken() {
        final Authentication authentication = spy(new AnonymousAuthenticationToken("dssg",
                                                                                   "htesn",
                                                                                   Arrays.asList((GrantedAuthority) () -> "dhfgj", (GrantedAuthority) () -> "dhfc")));

        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final boolean result = securityContextService.isUserFullyAuthenticated();

        assertThat(result).isFalse();
    }

    @Test
    public void getLoggedInUserProfile() {
        final Authentication authentication = mock(Authentication.class);
        final String userId = "fdsgzdg";
        final Principal principal = mock(Principal.class);
        final UserProfile userProfile = mock(UserProfile.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(userId);
        when(profileService.getUserProfile(principal)).thenReturn(userProfile);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final Optional<UserProfile> result = securityContextService.getLoggedInUserProfile();

        assertThat(result).containsSame(userProfile);
    }

    @Test
    public void getLoggedInUserProfile_noPrincipal() {
        final Authentication authentication = mock(Authentication.class);
        final String userId = "fdsgzdg";

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(null);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final Optional<UserProfile> result = securityContextService.getLoggedInUserProfile();

        assertThat(result).isEmpty();
    }

    @Test
    public void getLoggedInUserProfile_noUserProfile() {
        final Authentication authentication = mock(Authentication.class);
        final String userId = "fdsgzdg";
        final Principal principal = mock(Principal.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn(userId);
        when(profileService.getUserProfile(principal)).thenReturn(null);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final Optional<UserProfile> result = securityContextService.getLoggedInUserProfile();

        assertThat(result).isEmpty();
    }

    @Test
    public void getLoggedInUserProfile_noAuthentication() {
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(null);

        final Optional<UserProfile> result = securityContextService.getLoggedInUserProfile();

        assertThat(result).isEmpty();
    }

    @Test
    public void getLoggedInUserProfile_notAuthentciated() {
        final Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(false);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final Optional<UserProfile> result = securityContextService.getLoggedInUserProfile();

        assertThat(result).isEmpty();
    }

    @Test
    public void  getLoggedInUserProfile_AnonymousAuthenticationToken() {
        final Authentication authentication = spy(new AnonymousAuthenticationToken("dssg",
                                                                                   "htesn",
                                                                                   Arrays.asList((GrantedAuthority) () -> "dhfgj", (GrantedAuthority) () -> "dhfc")));

        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        final Optional<UserProfile> result = securityContextService.getLoggedInUserProfile();

        assertThat(result).isEmpty();
    }
}