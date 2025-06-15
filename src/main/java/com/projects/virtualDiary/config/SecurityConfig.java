package com.projects.virtualDiary.config;

import com.projects.virtualDiary.model.OAuth2LoginSuccessHandler;
import com.projects.virtualDiary.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private OAuth2LoginSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/login", "/error", "/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("/login/success", true)  // true - force redirection to specified url
//                        .failureUrl("/login/failure"));
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint()
                                .userService(new CustomOAuth2UserService()) // Custom service for OAuth2 user
                                .and()
                                .successHandler(successHandler));
//                .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

}
