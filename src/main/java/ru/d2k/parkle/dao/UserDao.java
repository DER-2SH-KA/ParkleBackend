package ru.d2k.parkle.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.dao.database.user.UserDatabaseSource;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.UserCache;
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
    private final UserDatabaseSource database;

    @Autowired
    private final UserMapper mapper;

    @Autowired
    private final RoleDao roleDao;

    public UserCache create(User entity) {
        return mapper.toCache(this.saveToDatabase(entity));
    }

    // TODO: Придумать и реализовать поиск списка пользователей (возможно, по срезу ключей).
    public Set<UserCache> getAll() {
        List<User> entities = this.getAllFromDatabase();

        log.debug("Users was taken from database!");

        return entities.stream()
                .map(mapper::toCache)
                .collect(Collectors.toSet());
    }

    // TODO: сделать поиск по ID (нюанс в том, что ключ состоит из Name).
    public Optional<UserCache> getById(UUID id) {
        Optional<User> entityFromDb = this.getFromDatabaseById(id);

        if (entityFromDb.isPresent()) {
            log.debug("User by id {} was taken from database!", id);

            return Optional.of(mapper.toCache(entityFromDb.get()));
        }

        return Optional.empty();
    }

    public Optional<UserCache> getByLogin(String login) {
        Optional<User> entityFromDb = this.getFromDatabaseByLogin(login);

        if (entityFromDb.isPresent()) {
            UserCache cacheFromEntity = mapper.toCache(entityFromDb.get());

            log.debug("User with login '{}' taken from database!", login);

            return Optional.of(cacheFromEntity);
        }

        return Optional.empty();
    }

    public User getReferenceById(UUID id) {
        return database.getReferenceById(id);
    }

    // TODO: Не обновляется роль. Роль сохраняется не в захэшированном виде.
    public Optional<UserCache> updateByLogin(String login, UserUpdateDto updateUserDto) {
        Optional<User> entity = this.getFromDatabaseByLogin(login);

        Optional<Role> role = roleDao.getFromDatabaseByName(updateUserDto.getRoleName());

        if (entity.isPresent() && role.isPresent()) {
            mapper.updateEntityByDto(entity.get(), updateUserDto, role.get());

            User updatedEntity = this.saveToDatabase(entity.get());
            UserCache cache = mapper.toCache(updatedEntity);

            return Optional.of(cache);
        }

        return Optional.empty();
    }

    public boolean deleteByLogin(String login) {
        Optional<User> entityToDelete = this.getFromDatabaseByLogin(login);

        if (entityToDelete.isPresent()) {
            this.deleteFromDatabaseByLogin(login);

            return !this.existInDatabaseByLogin(login);
        }

        log.error("User to delete with login '{}' not exist!", login);

        return true;
    }

    private User saveToDatabase(User entity) {
        return database.save(entity);
    }

    private List<User> getAllFromDatabase() {
        return database.getAll();
    }

    private Optional<User> getFromDatabaseById(UUID id) {
        return database.getById(id);
    }

    public Optional<User> getFromDatabaseByLogin(String login) {
        return database.getByLogin(login);
    }

    private void deleteFromDatabaseByLogin(String login) {
        database.deleteByLogin(login);
    }

    private boolean existInDatabaseByLogin(String login) {
        return database.existsByLogin(login);
    }
}