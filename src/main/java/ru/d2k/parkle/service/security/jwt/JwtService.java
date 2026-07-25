package ru.d2k.parkle.service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.security.cookie.CustomCookieService;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateTokenByUserCache(UserCache userCache) {
        Map<String, Object> claims = new HashMap<>();

        return this.createToken(claims, userCache.login());
    }

    public String getSubject(String jwt) {
        return this.getSubjectFromClaims(this.getClaims(jwt));
    }

    public boolean isTokenValid(String jwt, CustomUserDetails customUserDetails) {
        Claims claims = this.getClaims(jwt);

        return this.getSubjectFromClaims(claims).equals(customUserDetails.getUsername()) &&
                !this.isTokenExpiredByClaims(claims) && !this.isTokenUsedTooEarlyByClaims(claims);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .id(UuidGeneratorUtil.generateNewUuidV7().toString())
                .subject(subject)
                .notBefore(new Date(currentTimeMillis))
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + expiration))
                .signWith(this.getSignKey())
                .compact();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(this.getSignKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private String getSubjectFromClaims(Claims claims) {
        return claims.getSubject();
    }

    private Date getNotBeforeFromClaims(Claims claims) {
        return claims.getNotBefore();
    }

    private Date getExpirationFromClaims(Claims claims) {
        return claims.getExpiration();
    }

    private boolean isTokenUsedTooEarlyByClaims(Claims claims) {
        return this.getNotBeforeFromClaims(claims).after(new Date(System.currentTimeMillis()));
    }

    private boolean isTokenExpiredByClaims(Claims claims) {
        return this.getExpirationFromClaims(claims).before(new Date(System.currentTimeMillis()));
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}