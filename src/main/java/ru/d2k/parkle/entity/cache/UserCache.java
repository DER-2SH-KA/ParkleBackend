package ru.d2k.parkle.entity.cache;

import java.util.UUID;

public record UserCache(
        UUID id,
        String roleName,
        String login,
        String email,
        String hashedPassword,
        Boolean isBlocked
) { }
