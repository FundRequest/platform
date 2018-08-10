package io.fundrequest.social.gitter.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private String id;
    private String name;
    private String topic;
    private String uri;
    private Boolean oneToOne;
    private List<User> users;
    private Long userCount;
    private Long unreadItems;
    private Long mentions;
    private LocalDateTime lastAccessTime;
    private Boolean favourite;
    private Boolean lurk;
    private String url;
    private RoomType githubType;
    private List<String> tags;
    private String v;
}
