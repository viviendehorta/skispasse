package com.vdehorta.skispasse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    private static final String PUBLIC_MVC_PATTERN = "/api/public/**";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // @formatter:off
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PUBLIC_MVC_PATTERN).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        // @formatter:on
        return http.build();

    }
}
