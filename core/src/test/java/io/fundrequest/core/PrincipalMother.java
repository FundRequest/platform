package io.fundrequest.core;

import java.security.Principal;
import java.util.Objects;

public final class PrincipalMother {

    public static Principal davyvanroy() {
        return createPrincipal("davyvanroy");
    }

    private static Principal createPrincipal(String userId) {
        return new Principal() {
            @Override
            public String getName() {
                return userId;
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(getName());
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof Principal && ((Principal) obj).getName().equals(getName());
            }
        };
    }
}
