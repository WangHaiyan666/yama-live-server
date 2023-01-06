package com.newverse.yama.live.api.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newverse.yama.live.api.spec.HeaderName;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String MESSAGE_ID = "global.authentication-failed";

    @NonNull
    private final AuthenticationManager authenticationManager;
    @NonNull
    private final MessageSource message;
    @NonNull
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        val context = SecurityContextHolder.getContext();
        val previous = context.getAuthentication();

        try {
            if (previous == null || !previous.isAuthenticated()) {
                if (StringUtils.isEmpty(request.getHeader(HeaderName.USER_TOKEN))) {
                    throw new AuthenticationServiceException("Not Found Header: " + HeaderName.USER_TOKEN);
                }
                val authenticate = new UserTokenAuthentication(request.getHeader(HeaderName.USER_TOKEN), null,
                                                               null);
                val authenticateResult = authenticationManager.authenticate(authenticate);

                context.setAuthentication(authenticateResult);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            val errorResponse = new AuthenticationErrorResponse(
                    new AuthenticationErrorMessage(message.getMessage(MESSAGE_ID,
                                                                      null,
                                                                      MESSAGE_ID,
                                                                      request.getLocale()),
                                                   MESSAGE_ID));

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(mapper.writeValueAsString(errorResponse));
        }
    }

    @Value
    @RequiredArgsConstructor
    private static class AuthenticationErrorResponse {
        @NonNull
        private AuthenticationErrorMessage error;
    }

    @Value
    @RequiredArgsConstructor
    private static class AuthenticationErrorMessage {
        @NonNull
        private String message;
        @NonNull
        private String reason;
    }
}
