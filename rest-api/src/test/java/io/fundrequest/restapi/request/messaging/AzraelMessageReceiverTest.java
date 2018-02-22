package io.fundrequest.restapi.request.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.domain.ProcessedBlockchainEvent;
import io.fundrequest.core.request.fund.infrastructure.ProcessedBlockchainEventRepository;
import io.fundrequest.core.request.fund.messaging.dto.ClaimedEthDto;
import io.fundrequest.core.request.fund.messaging.dto.FundedEthDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AzraelMessageReceiverTest {

    private AzraelMessageReceiver messageReceiver;
    private FundService fundService;
    private ProcessedBlockchainEventRepository blockchainEventRepository;
    private ObjectMapper objectMapper;
    private RequestService requestService;

    @Before
    public void setUp() throws Exception {
        fundService = mock(FundService.class);
        blockchainEventRepository = mock(ProcessedBlockchainEventRepository.class);
        requestService = mock(RequestService.class);
        objectMapper = new ObjectMapper();
        messageReceiver = new AzraelMessageReceiver(requestService, objectMapper, blockchainEventRepository);
    }

    @Test
    public void receiveMessage() throws Exception {
        FundedEthDto dto = createDto();
        StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);
        when(blockchainEventRepository.findOne(dto.getTransactionHash())).thenReturn(Optional.empty());

        messageReceiver.receiveFundedMessage(w.toString());

        verifyRequestCreated(dto);
        verify(blockchainEventRepository).save(new ProcessedBlockchainEvent(dto.getTransactionHash()));
    }


    @Test
    public void receiveClaimMessage() throws Exception {
        ClaimedEthDto dto = createClaimedEthDto();
        StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);
        when(blockchainEventRepository.findOne(dto.getTransactionHash())).thenReturn(Optional.empty());

        messageReceiver.receiveClaimedMessage(w.toString());

        verifyRequestClaimed(dto);
        verify(blockchainEventRepository).save(new ProcessedBlockchainEvent(dto.getTransactionHash()));
    }

    public void verifyRequestClaimed(ClaimedEthDto dto) {
        RequestClaimedCommand command = new RequestClaimedCommand();
        command.setPlatform(Platform.valueOf(dto.getPlatform()));
        command.setPlatformId(dto.getPlatformId());
        command.setSolver(dto.getSolver());
        command.setAmountInWei(new BigDecimal(dto.getAmount()));
        command.setTimestamp(getTimeStamp(dto.getTimestamp()));
        command.setTransactionId(dto.getTransactionHash());
        verify(requestService).requestClaimed(command);
    }

    private LocalDateTime getTimeStamp(Long time) {
        return time == null ? null : Instant.ofEpochMilli(time)
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
    }



    private void verifyRequestCreated(FundedEthDto dto) {
        ArgumentCaptor<CreateRequestCommand> captor = ArgumentCaptor.forClass(CreateRequestCommand.class);
        verify(requestService).createRequest(captor.capture());
        assertThat(captor.getValue().getPlatformId()).isEqualTo(dto.getPlatformId());
        assertThat(captor.getValue().getPlatform().toString()).isEqualTo(dto.getPlatform());
        assertThat(captor.getValue().getFunds()).isEqualTo(dto.getAmount());
    }

    @Test
    public void receiveMessageTransactionAlreadyProcessed() throws Exception {
        FundedEthDto dto = createDto();
        StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);
        when(blockchainEventRepository.findOne(dto.getTransactionHash())).thenReturn(Optional.of(new ProcessedBlockchainEvent(dto.getTransactionHash())));

        messageReceiver.receiveFundedMessage(w.toString());

        verifyZeroInteractions(fundService);
    }

    private ClaimedEthDto createClaimedEthDto() {
        ClaimedEthDto dto = new ClaimedEthDto();
        dto.setAmount("5223000000000000000");
        dto.setPlatform("GITHUB");
        dto.setPlatformId("1");
        dto.setSolverAddress("0x");
        dto.setSolver("davyvanroy");
        dto.setTransactionHash("0xh");
        return dto;
    }

    private FundedEthDto createDto() {
        FundedEthDto dto = new FundedEthDto();
        dto.setAmount("5223000000000000000");
        dto.setPlatform("GITHUB");
        dto.setPlatformId("1");
        dto.setFrom("0x");
        dto.setTransactionHash("0xh");
        return dto;
    }
}