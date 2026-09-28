package ru.d2k.parkle.exception.entity;

import jakarta.persistence.EntityNotFoundException;

public class WebsiteTagEntityNotFoundException extends EntityNotFoundException {

    public WebsiteTagEntityNotFoundException(String message) {
        super(message);
    }
}
