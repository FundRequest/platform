package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.AddFundsCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.FundBuilder;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
class FundServiceImpl implements FundService {

    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;

    @Autowired
    public FundServiceImpl(FundRepository fundRepository, RequestRepository requestRepository, Mappers mappers) {
        this.fundRepository = fundRepository;
        this.requestRepository = requestRepository;
        this.mappers = mappers;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FundDto> findAll() {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findAll());
    }

    @Transactional
    @Override
    public void addFunds(Principal funder, AddFundsCommand command) {
        Request request = requestRepository.findOne(command.getRequestId())
                .orElseThrow(() -> new RuntimeException("Unable to find request"));
        Fund fund = FundBuilder.aFund()
                .withFunder(funder.getName())
                .withAmountInWei(command.getAmountInWei())
                .withRequestId(command.getRequestId())
                .build();
        fundRepository.save(fund);
        if (request.getStatus() == RequestStatus.OPEN) {
            request.setStatus(RequestStatus.FUNDED);
            requestRepository.save(request);
        }

    }
}
