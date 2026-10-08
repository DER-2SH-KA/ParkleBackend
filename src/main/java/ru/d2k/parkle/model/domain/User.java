package ru.d2k.parkle.model.domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record User(UUID id, String login, String hashedPassword, Set<Role> roles, List<UUID> websiteIds) { }