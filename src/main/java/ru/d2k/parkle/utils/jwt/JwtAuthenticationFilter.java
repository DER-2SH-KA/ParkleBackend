package ru.d2k.parkle.utils.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.security.cookie.CookieNames;
import ru.d2k.parkle.service.security.cookie.CustomCookieService;
import ru.d2k.parkle.service.security.jwt.JwtService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final CustomCookieService cookieService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> cookie = cookieService.fetchCookie(CookieNames.JWT_TOKEN, request);

        if (cookie.isEmpty()) {
            filterChain.doFilter(request, response);

            return;
        }

        Optional<String> jwt = cookieService.getValueFromCookie(cookie.get());

        if (jwt.isEmpty() || jwt.get().isBlank()) {
            filterChain.doFilter(request, response);

            return;
        }

        try {
            String login = jwtService.getSubject(jwt.get());

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails customUserDetails =
                        (CustomUserDetails) this.userDetailsService.loadUserByUsername(login);

                if (jwtService.isTokenValid(jwt.get(), customUserDetails)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                            null, customUserDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("Exception when JWT filter authentication process", ex);

            ResponseCookie responseCookie = cookieService.createCookieWithExpiredJwt();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/api/auth/login") || path.equals("/api/auth/registration")
                || path.equals("/api/auth/ping");
    }
}