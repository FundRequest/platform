package io.fundrequest.core.request.fund;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.command.PendingFundCommand;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigInteger;
import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PendingFundServiceTest {

    private PendingFundService pendingFundService;
    private PendingFundRepository pendingFundRepository;
    private GithubPlatformIdParser githubLinkParser;
    private Mappers mappers;
    private TokenInfoService tokenInfoService;

    @Before
    public void setUp() throws Exception {
        pendingFundRepository = mock(PendingFundRepository.class);
        githubLinkParser = mock(GithubPlatformIdParser.class);
        mappers = mock(Mappers.class);
        tokenInfoService = mock(TokenInfoService.class);
        pendingFundService = new PendingFundService(pendingFundRepository, githubLinkParser, mappers, tokenInfoService);
    }

    @Test
    public void save() {
        TokenInfoDto token = TokenInfoDtoMother.fnd();
        PendingFundCommand command = PendingFundCommand.builder()
                                                           .amount("1.2")
                                                           .description("description")
                                                           .fromAddress("0x0")
                                                           .tokenAddress(token.getAddress())
                                                           .platform(Platform.GITHUB)
                                                           .platformId("FundRequest|FR|area51|FR|12")
                                                           .transactionId("0x3")
                                                           .build();
        when(tokenInfoService.getTokenInfo(token.getAddress())).thenReturn(token);

        Principal davyvanroy = PrincipalMother.davyvanroy();
        pendingFundService.save(davyvanroy, command);

        ArgumentCaptor<PendingFund> pendingFundArgumentCaptor = ArgumentCaptor.forClass(PendingFund.class);
        verify(pendingFundRepository).save(pendingFundArgumentCaptor.capture());
        PendingFund pendingFund = pendingFundArgumentCaptor.getValue();
        assertThat(pendingFund.getAmount()).isEqualTo(new BigInteger("1200000000000000000"));
        assertThat(pendingFund.getTokenAddress()).isEqualTo(command.getTokenAddress());
        assertThat(pendingFund.getDescription()).isEqualTo(command.getDescription());
        assertThat(pendingFund.getTransactionHash()).isEqualTo(command.getTransactionId());
        assertThat(pendingFund.getFromAddress()).isEqualTo(command.getFromAddress());
        assertThat(pendingFund.getUserId()).isEqualTo(davyvanroy.getName());



    }
}