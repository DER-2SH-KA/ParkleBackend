package ru.d2k.parkle.service.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserDto;

import java.util.Collection;

@Service
@Primary
@RequiredArgsConstructor
public class ManualAuthenticationManagerService extends CustomAuthenticationManagerService {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication createHttpUnauthorizedAuthentication(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    @Override
    public Authentication createHttpUnauthorizedAuthentication(UserDto dto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
        );
    }

    @Override
    public Authentication createHttpAuthorizedAuthentication(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        authorities
                )
        );
    }
}
