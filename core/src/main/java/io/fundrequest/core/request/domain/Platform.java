package io.fundrequest.core.request.domain;

import java.util.Arrays;
import java.util.Optional;

public enum Platform {
    GITHUB,
    STACK_OVERFLOW;


    public static Optional<Platform> getPlatform(String platform) {
        return Arrays.stream(Platform.values()).filter(p -> p.toString().equalsIgnoreCase(platform)).findFirst();
    }
}
