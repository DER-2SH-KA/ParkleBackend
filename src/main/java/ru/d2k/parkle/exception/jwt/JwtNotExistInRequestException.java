package ru.d2k.parkle.exception.jwt;

public class JwtNotExistInRequestException extends JwtException {

    public JwtNotExistInRequestException(String message) {
        super(message);
    }
}