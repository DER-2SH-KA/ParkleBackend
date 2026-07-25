package ru.d2k.parkle.dao.database.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Primary
public class UserPostgresDatabase implements UserDatabaseSource{

    @Autowired
    private final UserRepository repository;

    @Override
    public User save(User entity) {
        return repository.save(entity);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User getReferenceById(UUID id) {
        return repository.getReferenceById(id);
    }

    @Override
    public void deleteByLogin(String login) {
        repository.deleteByLogin(login);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }
}