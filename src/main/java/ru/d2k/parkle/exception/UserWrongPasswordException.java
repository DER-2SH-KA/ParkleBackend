package ru.d2k.parkle.exception;

public class UserWrongPasswordException extends RuntimeException {
    public UserWrongPasswordException() { super(); }
    public UserWrongPasswordException(String message) { super(message); }
}
