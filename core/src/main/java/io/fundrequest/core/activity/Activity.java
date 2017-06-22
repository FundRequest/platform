package io.fundrequest.core.activity;

import java.time.LocalDateTime;

public class Activity {
    private String user;
    private String title;
    private String description;
    private LocalDateTime dateTime;

    public Activity(String user, String title, String description, LocalDateTime dateTime) {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getUser() {
        return user;
    }
}
