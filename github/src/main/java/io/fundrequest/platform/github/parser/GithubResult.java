package io.fundrequest.platform.github.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubResult {
    private String id;
    private String number;
    private String title;
    private String state;
    private String body;
    private GithubUser user;
}
