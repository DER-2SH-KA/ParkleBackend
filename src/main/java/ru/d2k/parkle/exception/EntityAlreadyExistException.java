package ru.d2k.parkle.exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException() { super(); }
    public EntityAlreadyExistException(String message) { super(message); }
}
