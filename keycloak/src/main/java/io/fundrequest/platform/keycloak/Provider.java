package io.fundrequest.platform.keycloak;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Provider {
    GITHUB,
    GOOGLE,
    LINKEDIN,
    TWITTER,
    STACKOVERFLOW;

    private static final Map<String, Provider> platformMap;

    static {
        platformMap = Arrays.stream(Provider.values()).collect(Collectors.toMap(p -> p.toString().toLowerCase(), Function.identity()));
    }

    public static Provider fromString(String p) {
        return p != null ? platformMap.get(p.toLowerCase()) : null;
    }
}
