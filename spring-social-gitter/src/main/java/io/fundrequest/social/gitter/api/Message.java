package io.fundrequest.social.gitter.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Message {

    private String id;
    private String text;
    private String html;
    private LocalDateTime sent;
    private LocalDateTime editedAt;
    private User fromUser;
    private Boolean unread;
    private Long readBy;
    private List<String> urls;
    private List<String> mentions;
    private List<String> issues;
    private List<String> meta;
    private Long v;

    public Message(final String text) {
        this.text = text;
    }
}
