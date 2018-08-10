package io.fundrequest.platform.gitter;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.social.gitter.api.Gitter;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

public class RequestFundedGitterHandler {

    private final Gitter gitter;
    private final ITemplateEngine githubTemplateEngine;
    private final FundService fundService;
    private final String platformBasePath;
    private final GitterService gitterService;

    public RequestFundedGitterHandler(final Gitter gitter,
                                      final ITemplateEngine githubTemplateEngine,
                                      final FundService fundService,
                                      final String platformBasePath,
                                      final GitterService gitterService) {
        this.gitter = gitter;
        this.githubTemplateEngine = githubTemplateEngine;
        this.fundService = fundService;
        this.platformBasePath = platformBasePath;
        this.gitterService = gitterService;
    }

    @EventListener
    @Async("taskExecutor")
    @Transactional(readOnly = true, propagation = REQUIRES_NEW)
    public void handle(final RequestFundedNotificationDto notification) {
        final FundDto fund = fundService.findOne(notification.getFundId());
        Context context = new Context();
        context.setVariable("platformBasePath", platformBasePath);
        context.setVariable("fund", fund);

        final List<String> fundedNotificationChannels = gitterService.listFundedNotificationRooms();
        if (fundedNotificationChannels.size() > 0) {
            final String message = githubTemplateEngine.process("notification-templates/request-funded-gitter", context);

            gitter.roomResource()
                  .listRooms()
                  .stream()
                  .filter(room -> fundedNotificationChannels.contains(room.getName()))
                  .forEach(room -> gitter.messageResource().sendMessage(room.getId(), message));
        }
    }
}
