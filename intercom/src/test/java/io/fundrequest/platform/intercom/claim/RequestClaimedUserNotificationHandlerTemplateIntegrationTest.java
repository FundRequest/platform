package io.fundrequest.platform.intercom.claim;

import io.fundrequest.common.FundRequestCommon;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.notification.FundRequestNotification;
import io.fundrequest.platform.github.FundRequestGithub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FundRequestNotification.class, FundRequestGithub.class, FundRequestCommon.class})
public class RequestClaimedUserNotificationHandlerTemplateIntegrationTest {

    @Autowired
    private ITemplateEngine githubTemplateEngine;

    @Test
    public void handle() {
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setId(56513L);
        final Context context = new Context();
        context.setVariable("platformBasepath", "http://fwrgdf");
        context.setVariable("request", request);

        final String messageBody = githubTemplateEngine.process("notification-templates/claimed-claimer-notification.html", context);

        assertThat(messageBody).isEqualTo("<html>\n"
                                          + "\n"
                                          + "<p>Hi {{ first_name | fallback:\"\" }},</p>\n"
                                          + "  \n"
                                          + "<p>Thank you for your code contribution. The reward linked to issue <a href=\"http://fwrgdf/requests/56513\">add hello world for python</a> has been"
                                          + " transferred to your account.</p>\n"
                                          + "\n"
                                          + "<p>Looking for more? Feel free to <a href=\"http://fwrgdf/requests\">browse</a> through all funded requests.</p>\n"
                                          + "\n"
                                          + "<p>&nbsp;</p>\n"
                                          + "<p>Kind regards,</p>\n"
                                          + "<p>The FundRequest team</p>\n"
                                          + "\n"
                                          + "</html>\n");
    }
}