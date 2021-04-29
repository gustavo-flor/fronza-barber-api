package com.github.gustavoflor.fronzabarberapi.infrastructure.delivery;

import com.github.gustavoflor.fronzabarberapi.core.Role;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Restrict {

    Role[] to();

}
