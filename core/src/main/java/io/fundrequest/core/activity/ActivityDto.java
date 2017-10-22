package io.fundrequest.core.activity;

import io.fundrequest.core.user.dto.UserDto;

import java.time.LocalDateTime;

public class ActivityDto {
    private UserDto user;
    private String title;
    private String description;
    private LocalDateTime dateTime;

    ActivityDto() {
    }

    public ActivityDto(UserDto user, String title, String description, LocalDateTime dateTime) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateTimeAsString() {
        return dateTime.toString();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public UserDto getUser() {
        return user;
    }
}
