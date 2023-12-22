package com.biljartline.billiardsapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

// sets authentication of request in SecurityContextHolder so that it may be checked later on in the chain
public class JWTAuthenticationFilter extends OncePerRequestFilter {
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

        // query userDetails from authentication API
        RestTemplate restTemplate = new RestTemplate();
        String baseURL = "http://localhost:8081";
        String getUserDetailsURL = baseURL + "/authentication/userdetails";
        // set header and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> userDetailsRequest = new HttpEntity<>(jwt, headers);
        // query userDetails
        ResponseEntity<String> userDetailResponse = restTemplate.postForEntity(getUserDetailsURL, userDetailsRequest, String.class);

        // throw errors at bad responses UNREACHABLE, SOMETHING ELSE THROWS AFTER BAD REQUEST
        if (userDetailResponse.getStatusCode() == HttpStatus.BAD_REQUEST)
            throw new BadCredentialsException("Invalid JWT");
        if (userDetailResponse.getStatusCode() != HttpStatus.OK)
            throw new AuthenticationException("Error querying authentication API");
        // read data as UserDetailsDTO
        ObjectMapper objectMapper = new ObjectMapper();
        UserDetailsDTO userDetailsDTO = objectMapper.readValue(userDetailResponse.getBody(), UserDetailsDTO.class);
        // convert to BiljartLineUser
        final BiljartLineUser user = BiljartLineUser.builder()
                .username(userDetailsDTO.username())
                .authorities(userDetailsDTO.roles().stream().map(SimpleGrantedAuthority::new).toList())
                .accountNonExpired(userDetailsDTO.isAccountNonExpired())
                .accountNonLocked(userDetailsDTO.isAccountNonLocked())
                .credentialsNonExpired(userDetailsDTO.isCredentialsNonExpired())
                .enabled(userDetailsDTO.isEnabled())
                .build();

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
