package ru.d2k.parkle.exception;

public class WebsiteNotFoundException extends RuntimeException {
    public WebsiteNotFoundException() { super(); }
    public WebsiteNotFoundException(String message) { super(message); }
}
