package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Me;
import io.fundrequest.social.gitter.api.UserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class UserResourceTemplateTest {

    private UserResource userResource;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        final RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        userResource = new UserResourceTemplate(restTemplate);
    }

    @Test
    void me() {
        mockServer.expect(requestTo("https://api.gitter.im/v1/user/me"))
                  .andExpect(method(GET))
                  .andRespond(withSuccess(new ClassPathResource("/user/me.json", getClass()), APPLICATION_JSON));

        final Me me = userResource.me();

        assertThat(me.getId()).isEqualTo("553d437215522ed4b3df8c50");
        assertThat(me.getUsername()).isEqualTo("MadLittleMods");
        assertThat(me.getDisplayName()).isEqualTo("Eric Eastwood");
        assertThat(me.getUrl()).isEqualTo("/MadLittleMods");
        assertThat(me.getAvatarUrl()).isEqualTo("https://avatars-05.gitter.im/gh/uv/3/MadLittleMods");
    }
}
