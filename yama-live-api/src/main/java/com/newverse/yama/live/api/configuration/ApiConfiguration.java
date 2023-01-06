package com.newverse.yama.live.api.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.newverse.yama.live.domain.DomainConfiguration;
import com.newverse.yama.live.infrastructure.InfrastructureConfiguration;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
@Import({
        DomainConfiguration.class,
        InfrastructureConfiguration.class
})
public class ApiConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        val  objectMapper = JsonMapper
                .builder()
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .build();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        val slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public MessageSource messageSource() {
        val messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger logger(@NonNull InjectionPoint ip) {
        return LoggerFactory
                .getLogger(Optional.ofNullable(ip.getMethodParameter())
                                   .<Class<?>>map(MethodParameter::getContainingClass)
                                   .orElseGet(() -> Optional.ofNullable(ip.getField())
                                                            .map(Field::getDeclaringClass)
                                                            .orElseThrow(() -> new BeanCreationException(
                                                                    "Cannot find type for Logger"))
                                   )
                );
    }
}
