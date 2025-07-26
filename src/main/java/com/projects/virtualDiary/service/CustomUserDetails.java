package com.projects.virtualDiary.service;

import com.projects.virtualDiary.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final boolean isGuest;

    public CustomUserDetails(User user) {
        this.user = user;
        this.isGuest = "guest@gmail.com".equalsIgnoreCase(user.getUserEmail());
    }

    public User getUserEntity() {
        return user;
    }

    public int getUserId() {
        return user != null ? user.getUserId() : -1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isGuest) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST"));
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null; // Not needed
    }

    @Override
    public String getUsername() {
        return user != null ? user.getUserEmail() : "guest@gmail.com";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user == null || !user.isLocked(); // safe for guest
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isGuest() {
        return isGuest;
    }
}
