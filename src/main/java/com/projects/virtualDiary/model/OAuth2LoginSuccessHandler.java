package com.projects.virtualDiary.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.virtualDiary.controller.PhotoController;
import com.projects.virtualDiary.service.JwtService;
import com.projects.virtualDiary.service.PhotoDiaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtService jwtService;

    @Autowired
    PhotoController controller;

    @Autowired
    PhotoDiaryServiceImpl service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Get user details from the authentication object
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        System.out.println("from onAuthenticationSuccess "+oAuth2User.getAttributes());
        String email = oAuth2User.getEmail();
        System.out.println("from onAuthenticationSuccess mailId : "+ oAuth2User.getEmail());
        User user = service.getUserByEmail(email);
        if(user == null){
            user = new User(oAuth2User.getName(),email,oAuth2User.getAttribute("picture"),false,new ArrayList<>());
            service.saveUser(user);
        };
        // Optional: Generate a token or store user information in a session
        String token = jwtService.generateToken(email); // Generate a token here (e.g., JWT)

        // Serialize the user data to JSON string
        String userJson = URLEncoder.encode(new ObjectMapper().writeValueAsString(user), "UTF-8");
        String tokenEncoded = URLEncoder.encode(token, "UTF-8");

        // Redirect to the frontend with the token
        String redirectUrl ="http://localhost:5173/login/success?token=" + token + "&user=" + userJson;
        response.sendRedirect(redirectUrl);
    }

    private String generateToken(String email) {
        // This is a mock for token generation. Replace with real JWT logic
        return Base64.getEncoder().encodeToString(email.getBytes());
    }
}
