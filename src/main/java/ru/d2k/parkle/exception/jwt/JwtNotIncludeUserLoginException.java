package ru.d2k.parkle.exception.jwt;

public class JwtNotIncludeUserLoginException extends JwtException {

    public JwtNotIncludeUserLoginException(String message) {
        super(message);
    }
}