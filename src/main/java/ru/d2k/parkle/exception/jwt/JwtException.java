package ru.d2k.parkle.exception.jwt;

public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
