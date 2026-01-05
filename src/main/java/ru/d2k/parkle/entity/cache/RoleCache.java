package ru.d2k.parkle.entity.cache;

import java.util.UUID;

public record RoleCache(
        UUID id,
        String name,
        Integer priority
) { }
