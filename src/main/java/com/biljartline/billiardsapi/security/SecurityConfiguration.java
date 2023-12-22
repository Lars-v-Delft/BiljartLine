package com.biljartline.billiardsapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    // defines filter-chain that is followed upon request
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // sets cors config
                .csrf(AbstractHttpConfigurer::disable)
                // sets authentication requirements for endpoints
                .authorizeHttpRequests(r -> r
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(HttpMethod.POST).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE).hasAuthority("ADMIN")
                        .anyRequest().hasAuthority("ADMIN"))
                // tells SecurityContextHolder? that these requests are stateless, necessary?
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // necessary?
                .httpBasic(Customizer.withDefaults())
                // add custom filter to chain
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
