package ru.d2k.parkle.dao.database.role;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
@RequiredArgsConstructor
public class RolePostgresDatabase implements RoleDatabaseSource {
    private final RoleRepository roleRepository;

    @Override
    public Role save(Role entity) {
        return roleRepository.save(entity);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> getByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role getReferenceById(UUID id) {
        return roleRepository.getReferenceById(id);
    }

    @Override
    public void deleteById(UUID id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return roleRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}
