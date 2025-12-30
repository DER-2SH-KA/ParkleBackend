package ru.d2k.parkle.exception;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() { super(); }
    public UserBlockedException(String message) { super(message); }
}
