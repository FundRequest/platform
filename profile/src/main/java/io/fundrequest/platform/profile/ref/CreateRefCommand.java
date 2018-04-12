package io.fundrequest.platform.profile.ref;

import lombok.Builder;
import lombok.Data;

import java.security.Principal;

@Data
@Builder
public class CreateRefCommand {
    private Principal principal;
    private String ref;
}
