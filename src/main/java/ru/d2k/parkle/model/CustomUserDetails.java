package ru.d2k.parkle.model;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails, CredentialsContainer {
    private String username;
    private String password;
    private Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

    private final ru.d2k.parkle.entity.cache.UserCache cache;

    public CustomUserDetails(ru.d2k.parkle.entity.cache.UserCache cache) {
        this.cache = cache;

        this.username = cache.login();
        this.password = cache.hashedPassword();
        this.grantedAuthorities = Stream
                .of(cache.roleName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public ru.d2k.parkle.entity.cache.UserCache getCache() {
        return this.cache;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
