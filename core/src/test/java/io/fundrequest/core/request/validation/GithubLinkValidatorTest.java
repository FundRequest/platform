package io.fundrequest.core.request.validation;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubLinkValidatorTest {

    private GithubLinkValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new GithubLinkValidator();
    }

    @Test
    public void illegalNotGithub() throws Exception {
        assertThat(
                validator.isValid("https://google.be", null)
        ).isFalse();
    }

    @Test
    public void illegalInvalidGithub() throws Exception {
        assertThat(
                validator.isValid("https://github.com/mybatis/spring/releases/tag/mybatis-spring-1.3.1", null)
        ).isFalse();
    }

    @Test
    public void validGithubLink() throws Exception {
        assertThat(
                validator.isValid("https://github.com/FundRequest/area51/issues/4", null)
        ).isTrue();
    }
}