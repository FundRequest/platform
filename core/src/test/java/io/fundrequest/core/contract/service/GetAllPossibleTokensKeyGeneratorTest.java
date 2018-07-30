package io.fundrequest.core.contract.service;

import io.fundrequest.core.request.domain.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllPossibleTokensKeyGeneratorTest {

    private KeyGenerator keyGenerator;
    private KeyGenerator fallbackKeyGenerator;

    private Method randomMethod = KeyGenerator.class.getMethods()[0];
    private String randomTarget = "";

    @BeforeEach
    void setUp() {
        fallbackKeyGenerator = mock(KeyGenerator.class);
        keyGenerator = new GetAllPossibleTokensKeyGenerator(fallbackKeyGenerator);
    }

    @Test
    void generate_GitHubKey() {
        final String owner = "sgfgs";
        final String repo = "szgff";
        final String platform = Platform.GITHUB.name();

        final String result = (String) keyGenerator.generate(randomTarget, randomMethod, platform, owner + "|FR|" + repo + "|FR|435");

        assertThat(result).isEqualTo(platform + "-" + owner + "-" + repo);
    }

    @Test
    void generate_OtherPlatformKey() {
        final String platform = Platform.STACK_OVERFLOW.name();
        final String platformId = "sgfgasfdgshd5";
        final String expected = "someKey";

        when(fallbackKeyGenerator.generate(randomTarget, randomMethod, platform, platformId)).thenReturn(expected);

        final String result = (String) keyGenerator.generate(randomTarget, randomMethod, platform, platformId);

        assertThat(result).isEqualTo(expected);
    }
}
