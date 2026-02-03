package ru.d2k.parkle.dao.database.user;

import ru.d2k.parkle.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDatabaseSource {
    User save(User entity);
    List<User> getAll();
    Optional<User> getById(UUID id);
    Optional<User> getByLogin(String login);
    User getReferenceById(UUID id);
    void deleteByLogin(String login);
    boolean existsByLogin(String login);
}
