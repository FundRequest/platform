package io.fundrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.request.messsaging.AzraelMessageReceiver;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.BlockchainEvent;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.command.RefundProcessedCommand;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.messaging.dto.ClaimedEthDto;
import io.fundrequest.core.request.fund.messaging.dto.FundedEthDto;
import io.fundrequest.core.request.fund.messaging.dto.RefundedEthDto;
import io.fundrequest.core.request.infrastructure.BlockchainEventRepository;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static io.fundrequest.core.request.domain.RequestMother.fundRequestArea51;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AzraelMessageReceiverTest {

    private AzraelMessageReceiver messageReceiver;
    private FundService fundService;
    private BlockchainEventRepository blockchainEventRepository;
    private ObjectMapper objectMapper;
    private RequestService requestService;
    private PendingFundService pendingFundService;
    private RefundService refundService;

    @Before
    public void setUp() throws Exception {
        fundService = mock(FundService.class);
        blockchainEventRepository = mock(BlockchainEventRepository.class);
        requestService = mock(RequestService.class);
        pendingFundService = mock(PendingFundService.class);
        objectMapper = new ObjectMapper();
        refundService = mock(RefundService.class);
        messageReceiver = new AzraelMessageReceiver(requestService, objectMapper, blockchainEventRepository, fundService, pendingFundService, refundService);
    }

    @Test
    public void receiveMessage() throws Exception {
        final FundedEthDto dto = createDto();
        final StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);
        final BlockchainEvent blockchainEvent = new BlockchainEvent(dto.getTransactionHash(), dto.getLogIndex());
        ReflectionTestUtils.setField(blockchainEvent, "id", 346L);
        final Long requestId = RequestDtoMother.freeCodeCampNoUserStories().getId();
        when(blockchainEventRepository.findByTransactionHashAndLogIndex(dto.getTransactionHash(), dto.getLogIndex())).thenReturn(Optional.empty());
        when(blockchainEventRepository.saveAndFlush(eq(new BlockchainEvent(dto.getTransactionHash(), dto.getLogIndex())))).thenReturn(blockchainEvent);
        when(requestService.createRequest(any())).thenReturn(requestId);

        messageReceiver.receiveFundedMessage(w.toString());

        verifyRequestCreated(dto);
        verify(fundService).addFunds(FundsAddedCommand.builder()
                                                      .requestId(requestId)
                                                      .amountInWei(new BigDecimal(dto.getAmount()))
                                                      .timestamp(getTimeStamp(dto.getTimestamp()))
                                                      .token(dto.getToken())
                                                      .funderAddress(dto.getFrom())
                                                      .transactionHash(dto.getTransactionHash())
                                                      .blockchainEventId(blockchainEvent.getId())
                                                      .build());
        verify(fundService).clearTotalFundsCache(requestId);
        verify(pendingFundService).removePendingFund(dto.getTransactionHash());
    }

    @Test
    public void receiveClaimMessage() throws Exception {
        final ClaimedEthDto dto = createClaimedEthDto();
        final StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);
        final BlockchainEvent blockchainEvent = new BlockchainEvent(dto.getTransactionHash(), dto.getLogIndex());
        ReflectionTestUtils.setField(blockchainEvent, "id", 346L);
        when(blockchainEventRepository.findByTransactionHashAndLogIndex(dto.getTransactionHash(), dto.getLogIndex())).thenReturn(Optional.empty());
        when(blockchainEventRepository.saveAndFlush(eq(new BlockchainEvent(dto.getTransactionHash(), dto.getLogIndex())))).thenReturn(blockchainEvent);

        final Request request = fundRequestArea51().withId(1L).build();
        when(requestService.requestClaimed(any(RequestClaimedCommand.class))).thenReturn(request);

        messageReceiver.receiveClaimedMessage(w.toString());

        verify(fundService).clearTotalFundsCache(request.getId());
        verify(requestService).requestClaimed(new RequestClaimedCommand(Platform.getPlatform(dto.getPlatform()).get(),
                                                                        dto.getPlatformId(),
                                                                        blockchainEvent.getId(),
                                                                        dto.getSolver(),
                                                                        getTimeStamp(dto.getTimestamp()),
                                                                        new BigDecimal(dto.getAmount()),
                                                                        dto.getToken()));
        verify(fundService).clearTotalFundsCache(request.getId());
    }

    @Test
    public void receiveClaimMessageTransactionAlreadyProcessed() throws Exception {
        ClaimedEthDto dto = createClaimedEthDto();
        StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);

        when(blockchainEventRepository.findByTransactionHashAndLogIndex(dto.getTransactionHash(), dto.getLogIndex())).thenReturn(Optional.of(mock(BlockchainEvent.class)));

        messageReceiver.receiveClaimedMessage(w.toString());

        verifyZeroInteractions(requestService);
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

        when(blockchainEventRepository.findByTransactionHashAndLogIndex(dto.getTransactionHash(), dto.getLogIndex())).thenReturn(Optional.of(mock(BlockchainEvent.class)));

        messageReceiver.receiveFundedMessage(w.toString());

        verifyZeroInteractions(fundService);
    }

    @Test
    public void receiveRefundedMessage() throws IOException {
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        final String transactionHash = "afdas";
        final String logIndex = "sgfdb";
        final String funderAddress = "0xFDHD756xfc";
        final RefundedEthDto refundedEthDto = RefundedEthDto.builder()
                                                            .transactionHash(transactionHash)
                                                            .logIndex(logIndex)
                                                            .platform(Platform.STACK_OVERFLOW.name())
                                                            .platformId("fadfv")
                                                            .amount("43000000000000000000")
                                                            .token("0x563457")
                                                            .owner(funderAddress)
                                                            .build();
        final StringWriter w = new StringWriter();
        objectMapper.writeValue(w, refundedEthDto);
        final RefundRequestDto refundRequestDto = RefundRequestDto.builder()
                                                                  .funderAddress(funderAddress)
                                                                  .build();
        final BlockchainEvent savedBlochainEvent = new BlockchainEvent(transactionHash, logIndex);
        savedBlochainEvent.setId(7564L);

        when(blockchainEventRepository.findByTransactionHashAndLogIndex(refundedEthDto.getTransactionHash(), refundedEthDto.getLogIndex())).thenReturn(Optional.empty());
        when(blockchainEventRepository.saveAndFlush(new BlockchainEvent(transactionHash, logIndex))).thenReturn(savedBlochainEvent);
        when(requestService.findRequest(Platform.valueOf(refundedEthDto.getPlatform()), refundedEthDto.getPlatformId())).thenReturn(request);

        messageReceiver.receiveRefundedMessage(w.toString());

        verify(blockchainEventRepository).saveAndFlush(new BlockchainEvent(transactionHash, logIndex));
        verify(refundService).refundProcessed(RefundProcessedCommand.builder()
                                                                    .requestId(request.getId())
                                                                    .funderAddress(funderAddress)
                                                                    .blockchainEventId(savedBlochainEvent.getId())
                                                                    .transactionHash(transactionHash)
                                                                    .tokenHash(refundedEthDto.getToken())
                                                                    .amount(refundedEthDto.getAmount())
                                                                    .build());
    }

    @Test
    public void receiveRefundedMessage_alreadyProcessed() throws IOException {
        RefundedEthDto dto = RefundedEthDto.builder().transactionHash("afdas").logIndex("sgfdb").platformId("fadfv").build();
        StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);

        when(blockchainEventRepository.findByTransactionHashAndLogIndex(dto.getTransactionHash(), dto.getLogIndex())).thenReturn(Optional.of(mock(BlockchainEvent.class)));

        messageReceiver.receiveRefundedMessage(w.toString());

        verifyZeroInteractions(fundService);
    }

    @Test
    public void receiveRefundedMessage_noPlatformId() throws IOException {
        RefundedEthDto dto = RefundedEthDto.builder().transactionHash("afdas").logIndex("sgfdb").platformId(null).build();
        StringWriter w = new StringWriter();
        objectMapper.writeValue(w, dto);

        when(blockchainEventRepository.findByTransactionHashAndLogIndex(dto.getTransactionHash(), dto.getLogIndex())).thenReturn(Optional.empty());

        messageReceiver.receiveRefundedMessage(w.toString());

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
        dto.setLogIndex("0xh11");
        return dto;
    }

    private FundedEthDto createDto() {
        return FundedEthDto.builder()
                           .platform("GITHUB")
                           .platformId("1")
                           .amount("5223000000000000000")
                           .token("0xe5dada80aa6477e85d09747f2842f7993d0df71c")
                           .from("0xbbee53d695c8f744b21b3ebb48c73700df375b49")
                           .transactionHash("0x4056a21aaec0e60a1ff76cfe5b08f3d3594e07de1aa88c93cc8ff0df0b9370f1")
                           .logIndex("0xh12")
                           .build();
    }
}
