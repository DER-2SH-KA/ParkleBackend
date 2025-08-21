package ru.d2k.parkle.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() { super(); }
    public RoleNotFoundException(String message) { super(message); }
}
