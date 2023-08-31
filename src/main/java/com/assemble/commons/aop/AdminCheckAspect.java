package com.assemble.commons.aop;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotAdminException;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.commons.exception.UnauthorizedException;
import com.assemble.user.domain.UserRole;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckAspect {

    private final UserContext userContext;
    private final UserRepository userRepository;

    @Before("@annotation(com.assemble.commons.annotation.AdminCheck)")
    public void doAdminCheck() {
        Long userId = userContext.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        if (!user.isAdmin()) {
            throw new NotAdminException();
        }
    }
}
