package io.fundrequest.core.request.fund.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private String userName;
    private String userUrl;
    private String userAvatar;
    private String title;
    private String body;
    private OffsetDateTime createdAt;
}
