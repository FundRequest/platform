package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.Fund;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FundRepository extends JpaRepository<Fund, Long> {

    List<Fund> findAllByRequestId(Long requestId);

    List<Fund> findAllByRequestIdIn(List<Long> requestIds);

    @Query(""
           + "SELECT new io.fundrequest.core.request.fund.infrastructure.TokenAmountDto(f.tokenValue.tokenAddress, SUM(f.tokenValue.amountInWei)) "
           + "FROM Fund f, Request r "
           + "WHERE f.requestId = r.id AND r.status = 'FUNDED' "
           + "GROUP BY f.tokenValue.tokenAddress")
    List<TokenAmountDto> getAmountPerTokenWhereRequestHasStatusFunded();


    @Query(""
           + "SELECT r.issueInformation.owner, f.tokenValue.tokenAddress, sum(f.tokenValue.amountInWei) "
           + "FROM Fund f, Request r "
           + "WHERE f.requestId = r.id AND r.status = 'FUNDED' "
           + "GROUP BY r.issueInformation.owner, f.tokenValue.tokenAddress")
    List<Object[]> getAmountPerTokenPerProjectWhereRequestHasStatusFunded();


    @Query(value = "SELECT "
                   + "  rt.technology, "
                   + "  f.token_hash, "
                   + "  sum(f.amount_in_wei) "
                   + "FROM fund f "
                   + "  JOIN request r ON f.request_id = r.id "
                   + "  JOIN request_technology rt ON r.id = rt.request_id "
                   + "WHERE r.status = 'FUNDED' "
                   + "GROUP BY rt.technology, f.token_hash",
           nativeQuery = true)
    List<Object[]> getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded();
}
