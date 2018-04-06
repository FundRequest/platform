package io.fundrequest.core.request.fund;

import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.TokenInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class FundServiceImpl implements FundService {

    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;
    private CacheManager cacheManager;
    private TokenInfoService tokenInfoService;

    @Autowired
    public FundServiceImpl(FundRepository fundRepository, RequestRepository requestRepository, Mappers mappers, ApplicationEventPublisher eventPublisher, CacheManager cacheManager, TokenInfoService tokenInfoService) {
        this.fundRepository = fundRepository;
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.eventPublisher = eventPublisher;
        this.cacheManager = cacheManager;
        this.tokenInfoService = tokenInfoService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FundDto> findAll() {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public List<FundDto> findAll(Iterable<Long> ids) {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findAll(ids));
    }


    @Override
    @Transactional(readOnly = true)
    public FundDto findOne(Long id) {
        return mappers.map(Fund.class, FundDto.class, fundRepository.findOne(id).orElseThrow(ResourceNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FundDto> findByRequestId(Long requestId) {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findByRequestId(requestId));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "funds", key = "#requestId")
    public List<TotalFundDto> getTotalFundsForRequest(Long requestId) {


        return fundRepository.findByRequestId(requestId).stream()
                .collect(Collectors.groupingBy(
                        Fund::getToken,
                        Collectors.mapping(Fund::getAmountInWei,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ))
                .entrySet().stream()
                .map(f -> createTotalFundDto(f.getKey(), f.getValue()))
                .collect(Collectors.toList());
    }

    private TotalFundDto createTotalFundDto(String token, BigDecimal totalAmount) {
        return TotalFundDto
                .builder()
                .tokenAddress(token)
                .tokenSymbol(tokenInfoService.getTokenInfo(token).getSymbol())
                .totalAmount(Convert.fromWei(totalAmount, Convert.Unit.ETHER))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<FundDto>> findByRequestIds(List<Long> requestIds) {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findByRequestIdIn(requestIds))
                .stream()
                .collect(Collectors.groupingBy(FundDto::getId));
    }

    @Transactional
    @Override
    public void addFunds(FundsAddedCommand command) {
        Request request = requestRepository.findOne(command.getRequestId())
                .orElseThrow(() -> new RuntimeException("Unable to find request"));
        Fund fund = Fund.builder()
                .amountInWei(command.getAmountInWei())
                .requestId(command.getRequestId())
                .token(command.getToken())
                .timestamp(command.getTimestamp())
                .build();
        fund = fundRepository.saveAndFlush(fund);
        cacheManager.getCache("funds").evict(fund.getId());
        if (request.getStatus() == RequestStatus.OPEN) {
            request.setStatus(RequestStatus.FUNDED);
            request = requestRepository.saveAndFlush(request);
        }
        eventPublisher.publishEvent(
                new RequestFundedEvent(
                        command.getTransactionId(), mappers.map(Fund.class, FundDto.class, fund),
                        mappers.map(Request.class, RequestDto.class, request),
                        command.getTimestamp())
        );

    }
}
