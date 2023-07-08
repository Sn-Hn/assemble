package com.assemble.annotation;


import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public @interface TestCustomProperty {
}
