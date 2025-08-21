package ru.d2k.parkle.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() { super(); }
    public UserNotFoundException(String message) { super(message); }
}
