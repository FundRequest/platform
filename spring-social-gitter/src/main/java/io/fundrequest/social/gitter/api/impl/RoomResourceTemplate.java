package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Room;
import io.fundrequest.social.gitter.api.RoomResource;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class RoomResourceTemplate implements RoomResource {

    private RestTemplate restTemplate;

    public RoomResourceTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Room> listRooms() {
        return Arrays.asList(restTemplate.getForEntity("https://api.gitter.im/v1/rooms", Room[].class).getBody());
    }
}
