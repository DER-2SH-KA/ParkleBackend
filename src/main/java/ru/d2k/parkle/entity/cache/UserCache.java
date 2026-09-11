package ru.d2k.parkle.entity.cache;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UserCache(UUID id, String login, String hashedPassword, Set<RoleCache> roles, List<UUID> websiteIds) { }