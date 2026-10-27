package ru.d2k.parkle.exception.entity;

import jakarta.persistence.EntityNotFoundException;

public class WebsiteEntityNotFoundException extends EntityNotFoundException {

    public WebsiteEntityNotFoundException(String message) {
        super(message);
    }
}