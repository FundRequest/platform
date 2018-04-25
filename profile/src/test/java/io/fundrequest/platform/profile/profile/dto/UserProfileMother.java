package io.fundrequest.platform.profile.profile.dto;

public final class UserProfileMother {
    public static UserProfile davy() {
        return UserProfile.builder()
                          .id("e7356d6a-4eff-4003-8736-557c36ce6e0c")
                          .name("Davy Van Roy")
                          .email("davy.van.roy@fundrequest.io")
                          .build();
    }
}