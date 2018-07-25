package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestClaimDtoDecoratorTest {

    private RequestClaimDtoDecorator decorator;
    private RequestClaimDtoMapper delegate;
    private RequestService requestService;

    @Before
    public void setUp() {
        delegate = mock(RequestClaimDtoMapper.class);
        requestService = mock(RequestService.class);

        decorator = new RequestClaimDtoDecorator() {};
        ReflectionTestUtils.setField(decorator, "delegate", delegate);
        ReflectionTestUtils.setField(decorator, "requestService", requestService);
        ReflectionTestUtils.setField(decorator, "platformBasePath", "https://fundrequest.io");
    }

    @Test
    public void map() {
        final RequestClaim requestClaim = RequestClaim.builder()
                                                      .id(657L)
                                                      .address("0xE51551D3B11eF7559164D051D9714E59A1c4E486")
                                                      .status(ClaimRequestStatus.PENDING)
                                                      .flagged(false)
                                                      .requestId(9L)
                                                      .solver("ghjgfhg")
                                                      .build();
        final RequestClaimDto requestClaimDto = new RequestClaimDto();
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();

        when(delegate.map(requestClaim)).thenReturn(requestClaimDto);
        when(requestService.findRequest(requestClaim.getRequestId())).thenReturn(requestDto);

        final RequestClaimDto result = decorator.map(requestClaim);

        assertThat(result.getTitle()).isEqualTo(requestDto.getIssueInformation().getTitle());
        assertThat(result.getUrl()).isEqualTo("https://github.com/" + issueInformation.getOwner() + "/" + issueInformation.getRepo() + "/issues/" + issueInformation.getNumber());
        assertThat(result.getFundRequestUrl()).isEqualTo("https://fundrequest.io/requests/" + requestDto.getId());
    }

    @Test
    public void map_null() {
        final RequestClaim requestClaim = RequestClaim.builder()
                                                      .id(657L)
                                                      .address("0xE51551D3B11eF7559164D051D9714E59A1c4E486")
                                                      .status(ClaimRequestStatus.PENDING)
                                                      .flagged(false)
                                                      .requestId(9L)
                                                      .solver("ghjgfhg")
                                                      .build();

        when(delegate.map(requestClaim)).thenReturn(null);

        final RequestClaimDto result = decorator.map(requestClaim);

        assertThat(result).isNull();
    }
}