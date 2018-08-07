package io.fundrequest.platform.gitter;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.social.gitter.api.Gitter;
import io.fundrequest.social.gitter.api.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

class RequestFundedGitterHandlerTest {

    private RequestFundedGitterHandler handler;

    private Gitter gitter;
    private ITemplateEngine templateEngine;
    private FundService fundService;
    private String platformBasePath;
    private List<Room> allRooms;
    private GitterService gitterService;

    @BeforeEach
    void setUp() {
        gitter = mock(Gitter.class, RETURNS_DEEP_STUBS);
        templateEngine = mock(ITemplateEngine.class);
        fundService = mock(FundService.class);
        gitterService = mock(GitterService.class);
        platformBasePath = "http://test.io";
        allRooms = Arrays.asList(Room.builder().name("FundRequest/funded-requests").id(UUID.randomUUID().toString()).build(),
                                 Room.builder().name("FundRequest/some-other-room").id(UUID.randomUUID().toString()).build(),
                                 Room.builder().name("Blablabla/yet-another-room").id(UUID.randomUUID().toString()).build());

        handler = new RequestFundedGitterHandler(gitter, templateEngine, fundService, platformBasePath, gitterService);
    }

    @Test
    void handle() {
        long fundId = 243L;
        long requestId = 764L;
        final String message = "ewfegdbf";
        final RequestFundedNotificationDto notification = RequestFundedNotificationDto.builder().fundId(fundId).requestId(requestId).build();
        final TokenValueDto tokenValue = TokenValueDtoMother.FND().build();
        final FundDto fund = FundDto.builder().tokenValue(tokenValue).build();
        final Context context = new Context();
        final List<String> fundedNotificationChannels = Arrays.asList(allRooms.get(0).getName(), allRooms.get(2).getName());
        context.setVariable("platformBasePath", platformBasePath);
        context.setVariable("fund", fund);

        when(fundService.findOne(fundId)).thenReturn(fund);
        when(templateEngine.process(eq("notification-templates/request-funded-gitter"), refEq(context, "locale"))).thenReturn(message);
        when(gitter.roomResource().listRooms()).thenReturn(allRooms);
        when(gitterService.listFundedNotificationRooms()).thenReturn(fundedNotificationChannels);

        handler.handle(notification);

        allRooms.stream()
                .filter(room -> fundedNotificationChannels.contains(room.getName()))
                .forEach(room -> verify(gitter.messageResource()).sendMessage(room.getId(), message));
        verifyNoMoreInteractions(gitter.messageResource());
    }

    @Test
    void handle_noFundedNotificationChannels() {
        long fundId = 243L;
        long requestId = 764L;
        final String message = "ewfegdbf";
        final RequestFundedNotificationDto notification = RequestFundedNotificationDto.builder().fundId(fundId).requestId(requestId).build();
        final TokenValueDto tokenValue = TokenValueDtoMother.FND().build();
        final FundDto fund = FundDto.builder().tokenValue(tokenValue).build();
        final Context context = new Context();
        final List<String> fundedNotificationChannels = Arrays.asList(allRooms.get(0).getName(), allRooms.get(2).getName());
        context.setVariable("platformBasePath", platformBasePath);
        context.setVariable("fund", fund);

        when(fundService.findOne(fundId)).thenReturn(fund);
        when(templateEngine.process(eq("notification-templates/request-funded-gitter"), refEq(context, "locale"))).thenReturn(message);
        when(gitterService.listFundedNotificationRooms()).thenReturn(new ArrayList<>());

        handler.handle(notification);

        verifyZeroInteractions(gitter);
    }
}
