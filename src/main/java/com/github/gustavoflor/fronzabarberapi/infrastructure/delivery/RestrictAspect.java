package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.Optional;

@Aspect
public class RestrictAspect {

    @Pointcut("@annotation(restrict)")
    public void restrictPointcut(Restrict restrict) {
        // Do nothing
    }

    @Before(value = "restrictPointcut(restrict)", argNames = "restrict")
    public void aroundAdvice(Restrict restrict) {
        Optional.ofNullable(AuthenticationContext.currentUser()).ifPresent(currentUser -> {
            if (Arrays.stream(restrict.to()).noneMatch(currentUser::hasAuthority)) {
                throw new SecurityException();
            }
        });
    }

}
