package ru.d2k.parkle.exception.entity;

import jakarta.persistence.EntityNotFoundException;

public class RoleEntityNotFoundException extends EntityNotFoundException {

    public RoleEntityNotFoundException(String message) {
        super(message);
    }
}