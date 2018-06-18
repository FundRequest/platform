package io.fundrequest.core.message.dto;

import io.fundrequest.core.message.domain.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {
    private Long id;
    private String name;
    private MessageType type;
    private String title;
    private String description;
    private String link;
}
