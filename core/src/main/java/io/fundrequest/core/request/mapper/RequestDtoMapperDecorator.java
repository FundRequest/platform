package io.fundrequest.core.request.mapper;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMapper;
import io.fundrequest.core.user.UserService;
import io.fundrequest.core.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    private SecurityContextService securityContextService;

    @Override
    public List<RequestDto> mapToList(Collection<? extends Request> collectionIn) {
        Map<Long, List<FundDto>> funds = getFunds(collectionIn);
        return collectionIn.stream().map(r -> this.map(r, funds.get(r.getId()))).collect(Collectors.toList());
    }

    private Map<Long, List<FundDto>> getFunds(Collection<? extends Request> collectionIn) {
        List<Long> requestIds = collectionIn.stream().map(Request::getId).collect(Collectors.toList());
        return fundService.findByRequestIds(requestIds);
    }

    @Override
    public Set<RequestDto> mapToSet(Collection<? extends Request> collectionIn) {
        Map<Long, List<FundDto>> funds = getFunds(collectionIn);
        return collectionIn.stream().map(r -> this.map(r, funds.get(r.getId()))).collect(Collectors.toSet());
    }

    @Override
    public Page<RequestDto> mapToPage(Page<? extends Request> pageIn) {
        Map<Long, List<FundDto>> funds = getFunds(pageIn.getContent());
        final List<RequestDto> collect = pageIn.getContent()
                .stream()
                .map(r -> this.map(r, funds.get(r.getId())))
                .collect(Collectors.toList());

        return new PageImpl<>(collect, null, pageIn.getTotalElements());
    }

    public RequestDto map(Request request) {
        return map(request, fundService.findByRequestId(request.getId()));
    }

    private RequestDto map(Request request, List<FundDto> funds) {
        RequestDto result = delegate.map(request);
        Authentication currentAuth = securityContextService.getLoggedInUser();
        if (result != null && currentAuth != null) {
            result.setLoggedInUserIsWatcher(request.getWatchers().contains(currentAuth.getName()));
            result.setWatchers(request.getWatchers().stream().map(this::getUser).filter(Objects::nonNull).collect(Collectors.toSet()));
        }
        if (result != null && !CollectionUtils.isEmpty(funds)) {
            BigDecimal totalFunds = funds.stream().map(FundDto::getAmountInWei).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalFunds != null) {
                result.setTotalFunds(Convert.fromWei(totalFunds, Convert.Unit.ETHER));
            }
        }

        return result;
    }

    private String getUser(String x) {
        UserDto user = userService.getUser(x);
        return user == null ? null : user.getEmail();
    }
}
