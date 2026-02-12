package ru.d2k.parkle.utils.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.d2k.parkle.service.security.cookie.CookieNames;
import ru.d2k.parkle.service.security.cookie.CustomCookieService;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomCookieService cookieService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        log.info("Request by path: {}", request.getContextPath() + request.getServletPath());

        Optional<Cookie> cookie = cookieService.fetchCookie(CookieNames.JwtToken, request);

        if (cookie.isEmpty()) {
            log.warn("Cookie is empty. Do Filter.");
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> jwtOptional = cookieService.getValueFromCookie(cookie.get());

        if (jwtOptional.isEmpty()) {
            log.warn("JWT is empty. Do Filter.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtOptional.get() instanceof String jwt) {

                if (jwt.isBlank()) {
                    log.warn("JWT is blank. Do Filter.");
                    filterChain.doFilter(request, response);
                    return;
                }

                String userLogin = jwtUtil.extractUsername(jwt);

                if (
                        Objects.nonNull(userLogin) &&
                                Objects.isNull(SecurityContextHolder.getContext().getAuthentication())
                ) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin);

                    if (jwtUtil.isTokenValid(jwt, userDetails)) {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        catch (Exception ex) {
            log.error("Exception when JWT filter authentication process: {}", ex);

            ResponseCookie responseCookie = jwtUtil.createJwtExpiredCookie();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
        finally {
            log.warn("At end. Do Filter.");
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/api/auth/login") ||
                path.equals("/api/auth/registration") ||
                path.equals("/api/auth/ping");
    }
}
