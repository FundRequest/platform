package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Room;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static io.fundrequest.social.gitter.api.RoomType.ONETOONE;
import static io.fundrequest.social.gitter.api.RoomType.ORG;
import static io.fundrequest.social.gitter.api.RoomType.ORG_CHANNEL;
import static io.fundrequest.social.gitter.api.RoomType.REPO;

public class RoomMother {

    public static Room room1() {
        final Room room = new Room();
        room.setId("53307860c3599d1de448e19d");
        room.setName("Andrew Newdigate");
        room.setTopic("");
        room.setOneToOne(true);
        room.setUsers(Arrays.asList(UserMother.user1()));
        room.setUnreadItems(0L);
        room.setMentions(0L);
        room.setLurk(false);
        room.setUrl("/suprememoocow");
        room.setGithubType(ONETOONE);
        return room;
    }

    public static Room room2() {
        final Room room = new Room();
        room.setId("5330777dc3599d1de448e194");
        room.setName("gitterHQ");
        room.setTopic("Gitter");
        room.setUri("gitterHQ");
        room.setOneToOne(false);
        room.setUserCount(2L);
        room.setUnreadItems(0L);
        room.setMentions(0L);
        room.setLastAccessTime(LocalDateTime.of(2014, 3, 24, 18, 22, 28, 105000000));
        room.setLurk(false);
        room.setUrl("/gitterHQ");
        room.setGithubType(ORG);
        room.setV("1");
        return room;
    }

    public static Room room3() {
        final Room room = new Room();
        room.setId("5330780dc3599d1de448e198");
        room.setName("gitterHQ/devops");
        room.setTopic("");
        room.setUri("gitterHQ/devops");
        room.setOneToOne(false);
        room.setUserCount(2L);
        room.setUnreadItems(0L);
        room.setMentions(0L);
        room.setLastAccessTime(LocalDateTime.of(2014, 3, 24, 18, 23, 10, 512000000));
        room.setLurk(false);
        room.setUrl("/gitterHQ/devops");
        room.setGithubType(ORG_CHANNEL);
        room.setV("1");
        return room;
    }

    public static Room room4() {
        final Room room = new Room();
        room.setId("53307793c3599d1de448e196");
        room.setName("malditogeek/vmux");
        room.setTopic("VMUX - Plugin-free video calls in your browser using WebRTC");
        room.setUri("malditogeek/vmux");
        room.setOneToOne(false);
        room.setUserCount(2L);
        room.setUnreadItems(0L);
        room.setMentions(0L);
        room.setLastAccessTime(LocalDateTime.of(2014, 3, 24, 18, 21, 8, 448000000));
        room.setFavourite(true);
        room.setLurk(false);
        room.setUrl("/malditogeek/vmux");
        room.setGithubType(REPO);
        room.setTags(Arrays.asList("javascript", "nodejs"));
        room.setV("1");
        return room;
    }

    public static List<Room> rooms() {
        return Arrays.asList(room1(), room2(), room3(), room4());
    }
}
