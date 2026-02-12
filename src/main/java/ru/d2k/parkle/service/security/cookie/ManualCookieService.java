package ru.d2k.parkle.service.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class ManualCookieService extends CustomCookieService {
    @Override
    public Optional<Cookie> fetchCookie(String name, HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, name));
    }

    @Override
    public Optional<String> getValueFromCookie(Cookie cookie) {
        return Objects.nonNull(cookie) ? Optional.of(cookie.getValue()) : Optional.empty();
    }

    @Override
    public ResponseCookie createResponseCookie(
            String name,
            String value,
            boolean httpOnly,
            boolean secure,
            String path,
            long maxAge,
            String sameSite
    ) {
        return ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .secure(secure)
                .path(path)
                .maxAge(maxAge)
                .sameSite(sameSite)
                .build();
    }

    @Override
    public ResponseCookie createEmptyResponseCookie(
            String name,
            boolean httpOnly,
            boolean secure,
            String path,
            String sameSite
    ) {
        return ResponseCookie.from(name, "")
                .httpOnly(httpOnly)
                .secure(secure)
                .path(path)
                .maxAge(0L)
                .sameSite(sameSite)
                .build();
    }
}
