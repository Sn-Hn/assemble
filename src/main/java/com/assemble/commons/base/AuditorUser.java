package com.assemble.commons.base;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuditorUser implements AuditorAware<Long> {

    private final UserContext userContext;

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(userContext.getUserId());
    }
}
