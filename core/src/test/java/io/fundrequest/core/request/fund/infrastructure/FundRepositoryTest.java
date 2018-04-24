package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.fundrequest.core.token.dto.TokenInfoDtoMother.fnd;
import static io.fundrequest.core.token.dto.TokenInfoDtoMother.zrx;
import static org.assertj.core.api.Assertions.assertThat;


public class FundRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    public void save() throws Exception {
        Request request = requestRepository.saveAndFlush(RequestMother.freeCodeCampNoUserStories().build());

        Fund fund = FundMother.fndFundFunderKnown().amountInWei(new BigDecimal("30")).requestId(request.getId()).build();

        fundRepository.saveAndFlush(fund);
    }

    @Test
    public void getAmountPerTokenPerProjectWhereRequestHasStatusFunded() {
        Request request = requestRepository.saveAndFlush(RequestMother.freeCodeCampNoUserStories().withStatus(RequestStatus.FUNDED).build());
        Request request2 = requestRepository.saveAndFlush(RequestMother.fundRequestArea51().withStatus(RequestStatus.FUNDED).build());
        Request request3 = requestRepository.saveAndFlush(RequestMother.freeCodeCampNoUserStories().withStatus(RequestStatus.CLAIMED).build());

        fundRepository.save(Arrays.asList(
                FundMother.zrxFundFunderKnown().requestId(request.getId()).amountInWei(new BigDecimal("10")).build(),

                FundMother.fndFundFunderKnown().requestId(request.getId()).amountInWei(new BigDecimal("30")).build(),
                FundMother.fndFundFunderKnown().requestId(request.getId()).amountInWei(new BigDecimal("10")).build(),

                FundMother.fndFundFunderKnown().requestId(request2.getId()).amountInWei(new BigDecimal("50")).build(),

                FundMother.fndFundFunderKnown().requestId(request3.getId()).amountInWei(new BigDecimal("50")).build()
                                         ));

        List<Object[]> result = fundRepository.getAmountPerTokenPerProjectWhereRequestHasStatusFunded();

        assertThat(result).hasSize(3);
        assertTechProject(result.get(0), "kazuki43zoo", zrx().getAddress(), "10");
        assertTechProject(result.get(1), "kazuki43zoo", fnd().getAddress(), "40");
        assertTechProject(result.get(2), "FundRequest", fnd().getAddress(), "50");
    }


    @Test
    public void getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded() {
        Request request = requestRepository.saveAndFlush(RequestMother.freeCodeCampNoUserStories().withStatus(RequestStatus.FUNDED).build());
        Request request2 = requestRepository.saveAndFlush(RequestMother.fundRequestArea51().withStatus(RequestStatus.FUNDED).build());
        Request request3 = requestRepository.saveAndFlush(RequestMother.freeCodeCampNoUserStories().withStatus(RequestStatus.CLAIMED).build());

        fundRepository.save(Arrays.asList(
                FundMother.zrxFundFunderKnown().requestId(request.getId()).amountInWei(new BigDecimal("10")).build(),
                FundMother.fndFundFunderKnown().requestId(request.getId()).amountInWei(new BigDecimal("30")).build(),
                FundMother.fndFundFunderKnown().requestId(request.getId()).amountInWei(new BigDecimal("10")).build(),

                FundMother.fndFundFunderKnown().requestId(request2.getId()).amountInWei(new BigDecimal("50")).build(),
                FundMother.zrxFundFunderKnown().requestId(request2.getId()).amountInWei(new BigDecimal("60")).build(),

                FundMother.fndFundFunderKnown().requestId(request3.getId()).amountInWei(new BigDecimal("90")).build()
                                         ));

        List<Object[]> result = fundRepository.getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded();

        assertThat(result).hasSize(6);
        assertTechProject(result.get(0), "kotlin", fnd().getAddress(), "50");
        assertTechProject(result.get(1), "python", zrx().getAddress(), "60");
        assertTechProject(result.get(2), "java", zrx().getAddress(), "10");
        assertTechProject(result.get(3), "kotlin", zrx().getAddress(), "60");
        assertTechProject(result.get(4), "python", fnd().getAddress(), "50");
        assertTechProject(result.get(5), "java", fnd().getAddress(), "40");
    }

    private void assertTechProject(Object[] objects, String techOrProject, String token, String amount) {
        assertThat(objects[0]).isEqualTo(techOrProject);
        assertThat(objects[1]).isEqualTo(token);
        assertThat(((BigDecimal) objects[2])).isEqualByComparingTo(new BigDecimal(amount));
    }
}