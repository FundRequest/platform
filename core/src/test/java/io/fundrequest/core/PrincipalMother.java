package io.fundrequest.core;

import java.security.Principal;

public final class PrincipalMother {

    public static Principal davyvanroy() {
        return () -> "davyvanroy";
    }
}
