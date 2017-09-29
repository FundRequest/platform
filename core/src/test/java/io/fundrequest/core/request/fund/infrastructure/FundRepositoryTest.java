package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.fund.domain.Fund;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class FundRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private FundRepository fundRepository;

    @Test
    public void save() throws Exception {
        Fund fund = FundMother.aFund().build();

        fundRepository.save(fund);
    }

}