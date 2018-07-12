package io.fundrequest.core.message.dto;

import io.fundrequest.core.message.domain.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String name;
    private MessageType type;
    private String title;
    private String description;
    private String link;
}
