package ru.d2k.parkle.entity.cache;

import java.util.List;
import java.util.UUID;

public record UserCache(
        UUID id,
        UUID roleId,
        String roleName,
        Integer rolePriority,
        String login,
        String email,
        String hashedPassword,
        Boolean isBlocked,
        List<UUID> websiteIds
) { }
