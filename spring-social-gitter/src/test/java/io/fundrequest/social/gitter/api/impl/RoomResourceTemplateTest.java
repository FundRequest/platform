package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Room;
import io.fundrequest.social.gitter.api.RoomResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class RoomResourceTemplateTest {

    private RoomResource roomResource;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        final RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        roomResource = new RoomResourceTemplate(restTemplate);
    }

    @Test
    void listRooms() {
        mockServer.expect(requestTo("https://api.gitter.im/v1/rooms"))
                  .andExpect(method(GET))
                  .andRespond(withSuccess(new ClassPathResource("/room/rooms.json", getClass()), APPLICATION_JSON));

        final List<Room> rooms = roomResource.listRooms();

        assertThat(rooms).isEqualTo(RoomMother.rooms());
    }
}