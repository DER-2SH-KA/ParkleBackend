package ru.d2k.parkle.service.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserDtoInterface;
import java.util.Collection;

@Service
public interface CustomAuthenticationManagerService {

    Authentication createHttpUnauthorizedAuthentication(UserDtoInterface userDto);
}