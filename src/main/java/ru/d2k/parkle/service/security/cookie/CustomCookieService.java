package ru.d2k.parkle.service.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public abstract class CustomCookieService {
    public abstract Optional<jakarta.servlet.http.Cookie> fetchCookie(String name, HttpServletRequest request);

    public abstract Optional<String> getValueFromCookie(Cookie cookie);

    public abstract ResponseCookie createResponseCookie(
            String name,
            String value,
            boolean httpOnly,
            boolean secure,
            String path,
            long maxAge,
            String sameSite
    );

    public abstract ResponseCookie createEmptyResponseCookie(
            String name,
            boolean httpOnly,
            boolean secure,
            String path,
            String sameSite
    );
}
