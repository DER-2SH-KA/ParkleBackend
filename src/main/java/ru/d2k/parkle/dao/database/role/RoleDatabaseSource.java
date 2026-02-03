package ru.d2k.parkle.dao.database.role;

import ru.d2k.parkle.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleDatabaseSource {
    Role save(Role entity);
    List<Role> getAll();
    Optional<Role> getById(UUID id);
    Optional<Role> getByName(String name);
    Role getReferenceById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByName(String name);
}
