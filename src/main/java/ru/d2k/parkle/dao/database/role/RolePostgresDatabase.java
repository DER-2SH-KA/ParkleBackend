package ru.d2k.parkle.dao.database.role;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Primary
@Component
public class RolePostgresDatabase implements RoleDatabaseSource {

    @Autowired
    private final RoleRepository repository;

    @Override
    public Role save(Role entity) {
        return repository.save(entity);
    }

    @Override
    public List<Role> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Role> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Role> getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Role getReferenceById(UUID id) {
        return repository.getReferenceById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}