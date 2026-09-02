package ru.d2k.parkle.dao.database.user;

import ru.d2k.parkle.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDatabase {

    UserEntity save(UserEntity entity);
    List<UserEntity> getAll();
    Optional<UserEntity> getById(UUID id);
    Optional<UserEntity> getByLogin(String login);
    UserEntity getReferenceById(UUID id);
    void deleteByLogin(String login);
    boolean existsByLogin(String login);
}