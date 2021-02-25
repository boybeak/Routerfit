package com.github.boybeak.irouter.core.annotation;

import com.github.boybeak.irouter.Interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouteTo {
    String value();
    Class<? extends Interceptor>[] interceptors() default {};
}
