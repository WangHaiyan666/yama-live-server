package com.newverse.yama.live.api.configuration;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.HEAD;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.TRACE;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newverse.yama.live.api.configuration.security.AuthenticationProcessingFilter;
import com.newverse.yama.live.api.configuration.security.CsrfProtectionFilter;
import com.newverse.yama.live.api.configuration.security.UserTokenAuthenticationProvider;
import com.newverse.yama.live.api.spec.HeaderName;
import com.newverse.yama.live.domain.service.UserService;
import com.newverse.yama.live.domain.spec.UserRole;
import java.util.EnumSet;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(3)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };
    @NonNull
    private final MessageSource messageSource;
    @NonNull
    private final ObjectMapper mapper;
    @NonNull
    private final UserService userService;

    @Bean
    public CsrfProtectionFilter csrfProtectionFilter() {
        return new CsrfProtectionFilter(
                new OrRequestMatcher(EnumSet.complementOf(EnumSet.of(GET, OPTIONS, HEAD, TRACE))
                                            .stream()
                                            .map(HttpMethod::name)
                                            .map(method -> new AntPathRequestMatcher("/api/**", method))
                                            .collect(Collectors.toList())));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new RequestHeaderRequestMatcher(HeaderName.USER_TOKEN))
            .addFilterBefore(createAuthenticationFilter(authenticationManager()),
                             UsernamePasswordAuthenticationFilter.class)
            // @formatter:off
            .csrf()
                .disable()
            .anonymous()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
            .authorizeRequests()
                .anyRequest().permitAll();
            // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new UserTokenAuthenticationProvider(userService));
    }

    private AuthenticationProcessingFilter createAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        return new AuthenticationProcessingFilter(authenticationManager, messageSource, mapper);
    }
}
