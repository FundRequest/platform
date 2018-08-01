package io.fundrequest.platform.twitter;

import io.fundrequest.common.FundRequestCommon;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.notification.FundRequestNotification;
import io.fundrequest.platform.github.FundRequestGithub;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FundRequestNotification.class, FundRequestGithub.class, FundRequestCommon.class}, properties = {"io.fundrequest.platform.base-path=http://test"})
public class TweetRequestFundedHandlerTemplateIntegrationTest {

    @Autowired
    private ITemplateEngine githubTemplateEngine;

    @Value("${io.fundrequest.platform.base-path}")
    private String platformBasePath;

    @Test
    public void handle() {
        FundDto fund = FundDtoMother.aFundDto().build();
        final Context context = new Context();
        context.setVariable("platformBasePath", platformBasePath);
        context.setVariable("fund", fund);

        final String messageBody = githubTemplateEngine.process("notification-templates/request-funded-tweet", context);

        assertThat(messageBody).isEqualTo("A new request just got funded with 3.87 FND. Find out more at http://test/requests/1\n");
    }
}