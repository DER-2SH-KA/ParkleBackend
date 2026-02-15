package ru.d2k.parkle.dao.database.role;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RolePostgresDatabaseTest {
    @Mock private RoleRepository roleRepository;
    @InjectMocks private RolePostgresDatabase rolePostgresDatabase;

    @Test
    @DisplayName("save(Role entity) - Should return saved Role entity")
    void save_shouldReturnRoleEntity() {
        Role entity = Role.create("USER", 10);

        when(roleRepository.save(entity))
                .thenReturn(entity);

        Role savedEntity = rolePostgresDatabase.save(entity);

        assertEquals(entity, savedEntity);
    }

    @Test
    @DisplayName("getAll() - Should return list of Role entities")
    void getAll_shouldReturnListOfRoleEntities() {
        List<Role> entities = List.of(
                Role.create("USER", 10),
                Role.create("ADMIN", 1)
        );

        when(roleRepository.findAll())
                .thenReturn(entities);

        List<Role> foundedEntity = rolePostgresDatabase.getAll();

        assertTrue(Arrays.deepEquals(
                entities.toArray(),
                foundedEntity.toArray()
        ));
    }

    @Test
    @DisplayName("getById(UUID id) - Should return Role entity by Id")
    void getById_shouldReturnRoleEntityById() {
        Role entity = Role.create("USER", 10);

        UUID id = entity.getId();

        when(roleRepository.findById(id))
                .thenReturn(Optional.of(entity));

        Optional<Role> foundedEntity = rolePostgresDatabase.getById(id);

        assertEquals(entity, foundedEntity.get());
    }

    @Test
    @DisplayName("getById(UUID id) - Should return Optional.empty() by Id")
    void getById_shouldReturnEmptyByIdWhenNotPresent() {
        UUID id = Uuid7Generator.generateNewUUID();

        when(roleRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Role> foundedEntity = rolePostgresDatabase.getById(id);

        assertEquals(Optional.empty(), foundedEntity);
    }

    @Test
    @DisplayName("getByName(String name) - Should return Role entity by name")
    void getByName_shouldReturnRoleEntityByName() {
        String name = "USER";
        Role entity = Role.create(name, 10);

        when(roleRepository.findByName(name))
                .thenReturn(Optional.of(entity));

        Optional<Role> foundedEntity = rolePostgresDatabase.getByName(name);

        assertEquals(entity, foundedEntity.get());
    }

    @Test
    @DisplayName("getById(UUID id) - Should return Optional.empty() by name")
    void getByName_shouldReturnEmptyByNameWhenNotPresent() {
        String name = "USER";

        when(roleRepository.findByName(name))
                .thenReturn(Optional.empty());

        Optional<Role> foundedEntity = rolePostgresDatabase.getByName(name);

        assertEquals(Optional.empty(), foundedEntity);
    }

    @Test
    @DisplayName("getReferenceById(String name) - Should return Role entity reference by Id")
    void getReferenceById_shouldReturnRoleReferenceById() {
        Role entity = Role.create("USER", 10);

        UUID id = entity.getId();

        when(roleRepository.getReferenceById(id))
                .thenReturn(entity);

        Role foundedEntity = rolePostgresDatabase.getReferenceById(id);

        assertEquals(entity, foundedEntity);
    }

    @Test
    @DisplayName("existsById(UUID id) - Should return true if entity exist by Id")
    void getReferenceById_shouldReturnTrueWhenExistsById() {
        UUID id = Uuid7Generator.generateNewUUID();

        when(roleRepository.existsById(id))
                .thenReturn(true);

        boolean isExist = rolePostgresDatabase.existsById(id);

        assertTrue(isExist);
    }

    @Test
    @DisplayName("existsById(UUID id) - Should return false if entity not exist by Id")
    void getReferenceById_shouldReturnFalseWhenNotExistById() {
        UUID id = Uuid7Generator.generateNewUUID();

        when(roleRepository.existsById(id))
                .thenReturn(false);

        boolean isExist = rolePostgresDatabase.existsById(id);

        assertFalse(isExist);
    }

    @Test
    @DisplayName("existsByName(String name) - Should return true if entity exists by name")
    void getReferenceByName_shouldReturnTrueWhenExistsById() {
        String name = "USER";

        when(roleRepository.existsByName(name))
                .thenReturn(true);

        boolean isExist = rolePostgresDatabase.existsByName(name);

        assertTrue(isExist);
    }

    @Test
    @DisplayName("existsByName(String name) - Should return false if entity not exist by name")
    void getReferenceByName_shouldReturnFalseWhenNotExistById() {
        String name = "USER";

        when(roleRepository.existsByName(name))
                .thenReturn(false);

        boolean isExist = rolePostgresDatabase.existsByName(name);

        assertFalse(isExist);
    }
}
