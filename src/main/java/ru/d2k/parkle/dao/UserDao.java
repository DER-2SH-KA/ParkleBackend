package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.user.UserDatabase;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.model.domain.User;
import ru.d2k.parkle.exception.entity.UserEntityNotFoundException;
import ru.d2k.parkle.utils.mapper.UserMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDao {

    @Autowired
    private final UserDatabase database;

    @Autowired
    private final UserMapper mapper;

    public User create(UserEntity entity) {
        UserEntity savedEntity = this.saveToDatabase(entity);

        return mapper.toDomain(savedEntity);
    }

    public Set<User> getAll() {
        List<UserEntity> entities = this.getAllFromDatabase();

        log.debug("Users was taken from database!");

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    public Optional<User> getById(UUID id) {
        Optional<UserEntity> entityFromDb = this.getByIdFromDatabase(id);

        if (entityFromDb.isPresent()) {
            log.debug("UserEntity by id {} was taken from database!", id);

            return Optional.of(mapper.toDomain(entityFromDb.get()));
        }

        return Optional.empty();
    }

    public Optional<User> getByLogin(String login) {
        Optional<UserEntity> entityFromDb = this.getByLoginFromDatabase(login);

        if (entityFromDb.isPresent()) {
            User cacheFromEntity = mapper.toDomain(entityFromDb.get());

            log.debug("UserEntity with login '{}' taken from database!", login);

            return Optional.of(cacheFromEntity);
        }

        return Optional.empty();
    }

    public UserEntity getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    // TODO: Преобразовать в Application/Domain-сущность вместо DTO.
    // TODO: Пока забей. Всё равно нет обновления сущности пользователя.
    public User updateByLogin(String login, UserUpdateDto updateUserDto) {
        UserEntity entity = this.getByLoginFromDatabase(login)
                .orElseThrow(() ->
                        new UserEntityNotFoundException("Can't find user with login " + login + " for update user entity"));

        /*RoleEntity role = roleDao.getByNameFromDatabase(updateUserDto.getRoleName())
                .orElseThrow(() ->
                        new UserEntityNotFoundException("Can't find role by name " + updateUserDto.getRoleName() +
                                " for update user entity"));

        String newPasswordHashed = passwordEncoder.encode(updateUserDto.getPassword());

        UserEntity updatedEntity = this.saveToDatabase(entity);

        return mapper.toCache(updatedEntity);*/

        log.warn("Updating user entity is not implemented in UserDao!");

        return mapper.toDomain(entity);
    }

    public boolean deleteByLogin(String login) {
        Optional<UserEntity> entityToDelete = this.getByLoginFromDatabase(login);

        if (entityToDelete.isPresent()) {
            this.deleteByLoginFromDatabase(login);

            return !this.existsByLoginInDatabase(login);
        }

        log.error("UserEntity to delete with login '{}' not exist!", login);

        return true;
    }

    public boolean existsByLogin(String login) {
        return this.existsByLoginInDatabase(login);
    }

    private UserEntity saveToDatabase(UserEntity entity) {
        return database.save(entity);
    }

    private List<UserEntity> getAllFromDatabase() {
        return database.getAll();
    }

    private Optional<UserEntity> getByIdFromDatabase(UUID id) {
        return database.getById(id);
    }

    public Optional<UserEntity> getByLoginFromDatabase(String login) {
        return database.getByLogin(login);
    }

    private void deleteByLoginFromDatabase(String login) {
        database.deleteByLogin(login);
    }

    private boolean existsByLoginInDatabase(String login) {
        return database.existsByLogin(login);
    }
}