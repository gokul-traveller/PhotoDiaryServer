package com.projects.virtualDiary.config;

import com.projects.virtualDiary.model.OAuth2LoginSuccessHandler;
import com.projects.virtualDiary.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {
    @Autowired
    private OAuth2LoginSuccessHandler successHandler;

    @Autowired
    private JwtFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/login", "/error", "/oauth2/**","/api/auth/guestLogin").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint()
                        .userService(new CustomOAuth2UserService())
                        .and()
                        .successHandler(successHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
