package ru.d2k.parkle.utils.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.d2k.parkle.exception.JwtNotExistInRequestException;
import ru.d2k.parkle.exception.JwtNotIncludeUserLoginException;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

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
        Optional<String> jwtOptional = JwtUtil.extractJwtFromCookie(request);

        if (jwtOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = jwtOptional
                    .orElseThrow(() ->
                            new JwtNotExistInRequestException("JWT token is null but extracting itself continued")
                );
            final String userLogin = jwtUtil.extractUsername(jwtOptional
                    .orElseThrow(() ->
                            new JwtNotIncludeUserLoginException("JWT token is null but extracting login from itself continued")
                    )
                );

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
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());

            ResponseCookie responseCookie = jwtUtil.createJwtExpiredCookie();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
        finally {
            filterChain.doFilter(request, response);
        }
    }
}
