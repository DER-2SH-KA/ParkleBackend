package ru.d2k.parkle.model;

import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.d2k.parkle.entity.cache.UserCache;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails, CredentialsContainer {

    private final String username;

    private String password;

    private Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

    @Getter
    private final UserCache cache;

    public CustomUserDetails(UserCache cache) {
        this.cache = cache;
        this.username = cache.login();
        this.password = cache.hashedPassword();
        this.grantedAuthorities = cache.roles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
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

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}