package com.projects.virtualDiary.controller;

import com.projects.virtualDiary.service.JwtService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GoogleAuthController {

    JwtService jwtService = new JwtService();

    @GetMapping("/login/success")
    @ResponseBody
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        String jwt = jwtService.generateToken(user.getAttribute("email"));
        return new HashMap<String, Object>(){{
                put("message", "Login successful");
                put("user", user.getAttributes());
                put("token", jwt);
    }};
    }

    @GetMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        return "An error occurred. Please check the logs.";
    }
}
