package com.projects.virtualDiary.service;

import com.projects.virtualDiary.Repo.UserRepository;
import com.projects.virtualDiary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if ("guest@gmail.com".equalsIgnoreCase(email)) {
            return new org.springframework.security.core.userdetails.User(
                    "guest@gmail.com",
                    "",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST"))
            );
        }

        User user = userRepo.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }
}
