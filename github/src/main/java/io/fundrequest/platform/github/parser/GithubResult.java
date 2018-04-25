package io.fundrequest.platform.github.parser;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GithubResult {
    private String id;
    private String number;
    private String title;
    private String state;
    private String body;

}
