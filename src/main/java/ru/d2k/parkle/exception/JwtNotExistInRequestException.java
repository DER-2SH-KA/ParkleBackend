package ru.d2k.parkle.exception;

public class JwtNotExistInRequestException extends RuntimeException {
    public JwtNotExistInRequestException() { super(); }
    public JwtNotExistInRequestException(String message) { super(message); }
}
