package ru.d2k.parkle.service.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface CustomCookieService {

    Optional<jakarta.servlet.http.Cookie> fetchCookie(String name, HttpServletRequest request);

    Optional<String> getValueFromCookie(Cookie cookie);

    ResponseCookie createResponseCookie(String name, String value, boolean httpOnly, boolean secure,
                                                        String path, long maxAge, String sameSite);

    ResponseCookie createEmptyResponseCookie(String name, boolean httpOnly, boolean secure,
                                                             String path, String sameSite);

    ResponseCookie createCookieWithJwt(String jwt);

    ResponseCookie createCookieWithExpiredJwt();
}