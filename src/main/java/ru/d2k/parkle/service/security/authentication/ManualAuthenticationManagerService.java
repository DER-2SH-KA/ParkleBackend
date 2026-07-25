package ru.d2k.parkle.service.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserDtoInterface;
import java.util.Collection;

@RequiredArgsConstructor
@Primary
@Service
public class ManualAuthenticationManagerService implements CustomAuthenticationManagerService {

    @Autowired
    private final AuthenticationManager manager;

    @Override
    public Authentication createHttpUnauthorizedAuthentication(UserDtoInterface userDto) {
        return manager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
    }
}