package io.fundrequest.platform.profile.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private String id;
    private String name;
    private String email;
    private String etherAddress;
    private String telegramName;
    private String picture;
    private String headline;
    private boolean verifiedDeveloper;
    private UserProfileProvider github;
    private UserProfileProvider linkedin;
    private UserProfileProvider twitter;
    private UserProfileProvider stackoverflow;
    private UserProfileProvider google;

    public boolean hadEtherAddress() {
        return etherAddress != null && etherAddress.length() > 0;
    }
}
