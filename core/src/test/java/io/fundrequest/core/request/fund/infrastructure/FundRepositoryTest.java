package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


public class FundRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    public void save() throws Exception {
        Request request = requestRepository.saveAndFlush(RequestMother.freeCodeCampNoUserStories().build());

        Fund fund = FundMother.aFund().withId(null).withAmountInWei(new BigDecimal("30")).withRequestId(request.getId()).build();

        fundRepository.saveAndFlush(fund);
    }

}