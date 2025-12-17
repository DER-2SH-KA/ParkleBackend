package ru.d2k.parkle.exception;

public class JwtNotIncludeUserLoginException extends RuntimeException {
    public JwtNotIncludeUserLoginException() { super(); }
    public JwtNotIncludeUserLoginException(String message) { super(message); }
}
