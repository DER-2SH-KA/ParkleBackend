package ru.d2k.parkle.service.security.cookie;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtCookieService {

    @Autowired
    private final CustomCookieService cookieService;

    public void setCookieWithJwtInResponse(String jwt, HttpServletResponse response) {
        ResponseCookie jwtCookie = cookieService.createCookieWithJwt(jwt);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
    }

    public void setExpiredJwtCookieInResponse(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.createCookieWithExpiredJwt().toString());
    }
}
