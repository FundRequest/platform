package io.fundrequest.platform.profile.message.dto;

import io.fundrequest.platform.profile.message.domain.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {

    private String name;
    private MessageType type;
    private String title;
    private String description;
    private String link;
}
