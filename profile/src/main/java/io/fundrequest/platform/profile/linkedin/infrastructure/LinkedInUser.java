package io.fundrequest.platform.profile.linkedin.infrastructure;

import lombok.Data;

@Data
public class LinkedInUser {
    private String id;
    private String firstName;
    private String lastName;
    private String headline;
}
