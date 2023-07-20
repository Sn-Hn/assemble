package com.assemble.commons.config;

import com.assemble.commons.converter.PageableConverter;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private TypeResolver typeResolver = new TypeResolver();

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
                        typeResolver.resolve(PageableConverter.class)))
                .apiInfo(swaggerInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
                .title("Assemble API Documentation")
                .description("Assemble 서비스 API 설명을 위한 문서입니다.")
                .license("Assemble")
                .version("1.0")
                .build();
    }
}
