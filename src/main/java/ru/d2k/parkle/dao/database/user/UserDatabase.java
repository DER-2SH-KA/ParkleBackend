package ru.d2k.parkle.dao.database.user;

import ru.d2k.parkle.dao.database.BasicCrudDatabase;
import ru.d2k.parkle.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserDatabase extends BasicCrudDatabase<UserEntity, UUID> {

    Optional<UserEntity> getByLogin(String login);
    void deleteByLogin(String login);
    boolean existsByLogin(String login);
}