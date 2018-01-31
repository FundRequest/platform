package io.fundrequest.core.request.fund;

import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.FundBuilder;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class FundServiceImpl implements FundService {

    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public FundServiceImpl(FundRepository fundRepository, RequestRepository requestRepository, Mappers mappers, ApplicationEventPublisher eventPublisher) {
        this.fundRepository = fundRepository;
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.eventPublisher = eventPublisher;
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

    @Transactional
    @Override
    public void addFunds(FundsAddedCommand command) {
        Request request = requestRepository.findOne(command.getRequestId())
                .orElseThrow(() -> new RuntimeException("Unable to find request"));
        Fund fund = FundBuilder.aFund()
                .withAmountInWei(command.getAmountInWei())
                .withRequestId(command.getRequestId())
                .withTimestamp(command.getTimestamp())
                .build();
        fund = fundRepository.saveAndFlush(fund);
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
