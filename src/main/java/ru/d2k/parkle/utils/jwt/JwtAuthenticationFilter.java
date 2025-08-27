package ru.d2k.parkle.utils.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        /*final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userLogin;

        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userLogin = jwtUtil.extractUsername(jwt);

        if (
                Objects.nonNull(userLogin) &&
                        Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
        ) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);*/

        String jwt = extractJwtFromCookie(request);

        if (Objects.isNull(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userLogin = jwtUtil.extractUsername(jwt);

        if (
                Objects.nonNull(userLogin) &&
                        Objects.isNull(SecurityContextHolder.getContext().getAuthentication())
        ) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "jwt-token");
        return Objects.nonNull(cookie) ? cookie.getValue() : null;
    }
}
