package io.fundrequest.core.user;

import org.junit.Before;
import org.junit.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private RealmResource realmResource;
    private UserResource userResource;

    @Before
    public void setUp() throws Exception {
        realmResource = mock(RealmResource.class, RETURNS_DEEP_STUBS);
        userService = new UserServiceImpl(realmResource);
    }

    @Test
    public void getUser() throws Exception {
        String username = "davy";
        userResource = mock(UserResource.class, RETURNS_DEEP_STUBS);
        when(userResource.toRepresentation().getEmail()).thenReturn("email");
        when(userResource.toRepresentation().getFirstName()).thenReturn("firstname");
        when(userResource.toRepresentation().getLastName()).thenReturn("lastname");
        when(userResource.toRepresentation().getAttributes().containsKey("picture")).thenReturn(true);
        when(userResource.toRepresentation().getAttributes().get("picture")).thenReturn(singletonList("pic.jpg"));
        when(realmResource.users().get(username)).thenReturn(userResource);

        UserDto result = userService.getUser(username);
        assertThat(result.getName()).isEqualTo("firstname lastname");
        assertThat(result.getEmail()).isEqualTo("email");
        assertThat(result.getPicture()).isEqualTo("pic.jpg");
    }


    @Test
    public void getMicrosoftUser() throws Exception {
        String username = "davy";
        userResource = mock(UserResource.class, RETURNS_DEEP_STUBS);
        when(userResource.toRepresentation().getEmail()).thenReturn("email");
        when(userResource.toRepresentation().getFirstName()).thenReturn("firstname");
        when(userResource.toRepresentation().getLastName()).thenReturn("lastname");
        when(userResource.toRepresentation().getAttributes().containsKey("microsoft_id")).thenReturn(true);
        when(userResource.toRepresentation().getAttributes().get("microsoft_id")).thenReturn(singletonList("1"));
        when(realmResource.users().get(username)).thenReturn(userResource);

        UserDto result = userService.getUser(username);
        assertThat(result.getName()).isEqualTo("firstname lastname");
        assertThat(result.getEmail()).isEqualTo("email");
        assertThat(result.getPicture()).isEqualTo("https://apis.live.net/v5.0/1/picture?type=small");

    }
}