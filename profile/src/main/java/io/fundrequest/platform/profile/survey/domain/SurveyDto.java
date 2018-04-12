package io.fundrequest.platform.profile.survey.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SurveyDto {
    private LocalDateTime completionDate;
    private SurveyStatus status;
}
