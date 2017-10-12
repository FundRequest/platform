package io.fundrequest.restapi.config;


import feign.FeignException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
public class CivicAuthClientTest {

    @Autowired
    private CivicAuthClient civicAuthClient;

    @Test
    @Ignore
    public void blah() throws Exception {
        assertThatThrownBy(() -> civicAuthClient.getIssue("token"))
                .hasCauseInstanceOf(FeignException.class);
    }
}