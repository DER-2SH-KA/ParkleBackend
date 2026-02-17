package ru.d2k.parkle.service.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.d2k.parkle.dto.UserDto;

import java.util.Collection;

public abstract class CustomAuthenticationManagerService {
    public abstract Authentication createHttpUnauthorizedAuthentication(String username, String password);

    public abstract Authentication createHttpUnauthorizedAuthentication(UserDto dao);

    public abstract Authentication createHttpAuthorizedAuthentication(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    );
}
