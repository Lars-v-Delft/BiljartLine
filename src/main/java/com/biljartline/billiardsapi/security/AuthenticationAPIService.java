package com.biljartline.billiardsapi.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.sasl.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthenticationAPIService {
    private final RestTemplate restTemplate;
    public BiljartLineUser getUserFromJWT(String jwt) throws AuthenticationException, JsonProcessingException {
        // query userDetails from authentication API
        String getUserDetailsURL = System.getenv("AUTHENTICATION_API_URL") + "/authentication/userdetails";
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
        return BiljartLineUser.builder()
                .username(userDetailsDTO.username())
                .authorities(userDetailsDTO.roles().stream().map(SimpleGrantedAuthority::new).toList())
                .accountNonExpired(userDetailsDTO.isAccountNonExpired())
                .accountNonLocked(userDetailsDTO.isAccountNonLocked())
                .credentialsNonExpired(userDetailsDTO.isCredentialsNonExpired())
                .enabled(userDetailsDTO.isEnabled())
                .build();
    }
}
