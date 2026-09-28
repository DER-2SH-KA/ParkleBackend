package ru.d2k.parkle.exception.entity;

import jakarta.persistence.EntityNotFoundException;

public class UserEntityNotFoundException extends EntityNotFoundException {

    public UserEntityNotFoundException(String message) {
        super(message);
    }
}