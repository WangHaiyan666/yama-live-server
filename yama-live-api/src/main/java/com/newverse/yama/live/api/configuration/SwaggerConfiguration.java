package com.newverse.yama.live.api.configuration;

import com.google.common.base.Predicates;
import com.newverse.yama.live.api.spec.HeaderName;
import java.util.List;
import java.util.Optional;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket document() {
        val globalRequestParameters = List.of(new ParameterBuilder()
                                                      .name(HeaderName.USER_TOKEN)
                                                      .description("For authentication.")
                                                      .modelRef(new ModelRef("string"))
                                                      .parameterType("header")
                                                      .required(false)
                                                      .build(),
                                              new ParameterBuilder()
                                                      .name(HeaderName.CSRF_TOKEN)
                                                      .description(
                                                              "For prevent csrf.(except GET method)")
                                                      .modelRef(new ModelRef("string"))
                                                      .parameterType("header")
                                                      .required(false)
                                                      .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(Optional.class)
                .globalOperationParameters(globalRequestParameters)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.newverse.yama.live.api.controller"))
                .paths(Predicates.or(PathSelectors.ant("/api/**")::apply))
                .build().apiInfo(new ApiInfoBuilder()
                                         .title("Yama live service")
                                         .description("API of Yama live")
                                         .version("1.0")
                                         .build());
    }
}
