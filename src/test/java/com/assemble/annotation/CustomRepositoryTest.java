package com.assemble.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@TestCustomProperty
@DataJpaTest
@ExtendWith(SpringExtension.class)
public @interface CustomRepositoryTest {
}
