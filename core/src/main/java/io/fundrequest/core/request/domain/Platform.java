package io.fundrequest.core.request.domain;

public enum Platform {
    GITHUB("github", 1),
    STACK_OVERFLOW("stack-overflow", 2);

    private final String key;
    private final Integer value;

    Platform(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public Integer getValue() {
        return value;
    }
}
