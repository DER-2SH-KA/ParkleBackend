package ru.d2k.parkle.dao.database.role;

import ru.d2k.parkle.dao.database.BasicCrudDatabase;
import ru.d2k.parkle.entity.RoleEntity;
import java.util.Optional;
import java.util.UUID;

public interface RoleDatabase extends BasicCrudDatabase<RoleEntity, UUID> {

    Optional<RoleEntity> getByName(String name);
    boolean existsByName(String name);
}