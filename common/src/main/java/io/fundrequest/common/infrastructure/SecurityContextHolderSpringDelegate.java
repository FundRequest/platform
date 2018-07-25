package io.fundrequest.common.infrastructure;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHolderSpringDelegate {

    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    public void setContext(SecurityContext context) {
        SecurityContextHolder.setContext(context);
    }

    public int getInitializeCount() {
        return SecurityContextHolder.getInitializeCount();
    }

    public void setStrategyName(String strategyName) {
        SecurityContextHolder.setStrategyName(strategyName);
    }

    public SecurityContextHolderStrategy getContextHolderStrategy() {
        return SecurityContextHolder.getContextHolderStrategy();
    }

    public SecurityContext createEmptyContext() {
        return SecurityContextHolder.createEmptyContext();
    }
}
