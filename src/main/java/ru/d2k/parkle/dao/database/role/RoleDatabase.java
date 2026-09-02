package ru.d2k.parkle.dao.database.role;

import ru.d2k.parkle.entity.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleDatabase {

    RoleEntity save(RoleEntity entity);
    List<RoleEntity> getAll();
    Optional<RoleEntity> getById(UUID id);
    Optional<RoleEntity> getByName(String name);
    RoleEntity getReferenceById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByName(String name);
}