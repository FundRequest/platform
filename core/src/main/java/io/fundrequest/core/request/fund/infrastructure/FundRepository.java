package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.Fund;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FundRepository extends JpaRepository<Fund, Long> {

    List<Fund> findByRequestId(Long requestId);

    List<Fund> findByRequestIdIn(List<Long> requestIds);

    @Query(""
           + "select new io.fundrequest.core.request.fund.infrastructure.TokenAmountDto(f.token, sum(f.amountInWei)) "
           + "from Fund f, Request r "
           + "where f.requestId = r.id and r.status = 'FUNDED' "
           + "group by f.token")
    List<TokenAmountDto> getAmountPerTokenWhereRequestHasStatusFunded();


    @Query(""
           + "select r.issueInformation.owner, f.token, sum(f.amountInWei) "
           + "from Fund f, Request r "
           + "where f.requestId = r.id and r.status = 'FUNDED' "
           + "group by r.issueInformation.owner, f.token")
    List<Object[]> getAmountPerTokenPerProjectWhereRequestHasStatusFunded();


    @Query(value = "SELECT "
                   + "  rt.technology, "
                   + "  f.token, "
                   + "  sum(f.amount_in_wei) "
                   + "FROM fund f "
                   + "  JOIN request r ON f.request_id = r.id "
                   + "  JOIN request_technology rt ON r.id = rt.request_id "
                   + "WHERE r.status = 'FUNDED' "
                   + "GROUP BY rt.technology, f.token", nativeQuery = true)
    List<Object[]> getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded();

}
