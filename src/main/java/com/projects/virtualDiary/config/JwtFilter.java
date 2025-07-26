package com.projects.virtualDiary.config;

import com.projects.virtualDiary.model.User;
import com.projects.virtualDiary.service.CustomUserDetails;
import com.projects.virtualDiary.service.JwtService;
import com.projects.virtualDiary.service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");

        // ⛔ Skip JWT validation for guest login and any other public routes
        if ("/api/auth/guestLogin".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("🔍 JwtAuthFilter: Incoming request to " + path);
        System.out.println("🔑 Authorization Header: " + authHeader);

        String jwt = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                userEmail = jwtService.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println("❌ JWT expired: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT token expired");
                return;
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                System.out.println("❌ Invalid JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("❌ JWT extraction failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token parsing error");
                return;
            }
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;

            if (userEmail.equalsIgnoreCase("guest@gmail.com")) {
                User guest = new User(
                        0,
                        "Guest",
                        "guest@gmail.com",
                        "photo", // photo URL or placeholder
                        false,   // isLocked
                        new ArrayList<>() // categories or whatever your User constructor takes
                );
                userDetails = new CustomUserDetails(guest);
            } else {
                userDetails = userDetailsService.loadUserByUsername(userEmail);
            }

            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }


        chain.doFilter(request, response);
    }
}
