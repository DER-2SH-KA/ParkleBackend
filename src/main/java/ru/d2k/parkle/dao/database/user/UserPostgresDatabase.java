package ru.d2k.parkle.dao.database.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.repository.UserEntityRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Primary
public class UserPostgresDatabase implements UserDatabase {

    @Autowired
    private final UserEntityRepository repository;

    @Override
    public UserEntity save(UserEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<UserEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserEntity> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserEntity> getByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public UserEntity getReferenceById(UUID id) {
        return repository.getReferenceById(id);
    }

    @Override
    public void deleteById(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Override
    public void deleteByLogin(String login) {
        repository.deleteByLogin(login);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return repository.existsById(uuid);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }
}