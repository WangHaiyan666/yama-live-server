package com.newverse.yama.live.api.configuration.security;

import com.newverse.yama.live.api.spec.HeaderName;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class CsrfProtectionFilter extends OncePerRequestFilter implements Ordered {
    @NonNull
    private final RequestMatcher requestMatcher;

    @Getter
    @SuppressWarnings("FieldMayBeFinal")
    private int order = Ordered.HIGHEST_PRECEDENCE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (StringUtils.isEmpty(request.getHeader(HeaderName.CSRF_TOKEN)) && requestMatcher.matches(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
