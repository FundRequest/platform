package io.fundrequest.core.request.mapper;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.request.view.AllFundsDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMapper;
import io.fundrequest.core.user.UserService;
import io.fundrequest.core.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class RequestDtoMapperDecorator implements RequestDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private RequestDtoMapper delegate;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private FundService fundService;

    @Autowired
    private FiatService fiatService;

    @Autowired
    private SecurityContextService securityContextService;

    public RequestDto map(Request request) {
        return map(request, fundService.getTotalFundsForRequest(request.getId()));
    }

    private RequestDto map(Request request, List<TotalFundDto> totalFunds) {
        RequestDto result = delegate.map(request);
        Authentication currentAuth = securityContextService.getLoggedInUser();
        if (result != null && currentAuth != null) {
            result.setLoggedInUserIsWatcher(request.getWatchers().contains(currentAuth.getName()));
            result.setWatchers(request.getWatchers().stream().map(this::getUser).filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        if (result != null && totalFunds != null) {
            mapFunds(totalFunds, result);
        }

        return result;
    }

    private void mapFunds(List<TotalFundDto> totalFunds, RequestDto result) {
        AllFundsDto funds = result.getFunds();
        funds.setFndFunds(totalFunds.stream().filter(f -> "FND".equalsIgnoreCase(f.getTokenSymbol())).findFirst().orElse(null));
        funds.setOtherFunds(totalFunds.stream().filter(f -> !"FND".equalsIgnoreCase(f.getTokenSymbol())).findFirst().orElse(null));
        funds.setUsdFunds(fiatService.getUsdPrice(funds.getFndFunds(), funds.getOtherFunds()));
    }

    private String getUser(String x) {
        UserDto user = userService.getUser(x);
        return user == null ? null : user.getEmail();
    }
}
