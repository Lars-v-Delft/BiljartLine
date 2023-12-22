package com.biljartline.billiardsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
// sets authentication of request in SecurityContextHolder so that it may be checked later on in the chain
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationAPIService authenticationAPIService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // if header has no jwt, no authentication has to be set and this filter can be skipped
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authorizationHeader.substring(7);

        BiljartLineUser user = authenticationAPIService.getUserFromJWT(jwt);

        // add authentication to SecurityContextHolder
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // continue on the chain
        filterChain.doFilter(request, response);
    }
}
