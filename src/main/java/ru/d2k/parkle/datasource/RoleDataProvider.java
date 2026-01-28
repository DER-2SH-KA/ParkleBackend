package ru.d2k.parkle.datasource;

import java.util.Optional;
import java.util.UUID;

public interface RoleDataProvider<C> {
    Optional<C> getByName(String name);
    boolean existByName(String name);
    boolean existsById(UUID id);
}
