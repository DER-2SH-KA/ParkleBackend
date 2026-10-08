package ru.d2k.parkle.dao.database.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.repository.RoleEntityRepository;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RoleEntityPostgresDatabaseTest {

    @Mock
    private RoleEntityRepository roleEntityRepository;

    @InjectMocks
    private RolePostgresDatabase rolePostgresDatabase;

    @Test
    @DisplayName("save(RoleEntity entity) - Should return saved RoleEntity entity")
    void save_shouldReturnRoleEntity() {
        RoleEntity entity = RoleEntity.create("USER");

        when(roleEntityRepository.save(entity)).thenReturn(entity);

        RoleEntity savedEntity = rolePostgresDatabase.save(entity);

        assertEquals(entity, savedEntity);
    }

    @Test
    @DisplayName("getAll() - Should return list of RoleEntity entities")
    void getAll_shouldReturnListOfRoleEntities() {
        List<RoleEntity> entities = List.of(
                RoleEntity.create("USER"),
                RoleEntity.create("ADMIN")
        );

        when(roleEntityRepository.findAll())
                .thenReturn(entities);

        List<RoleEntity> foundedEntity = rolePostgresDatabase.getAll();

        assertTrue(Arrays.deepEquals(
                entities.toArray(),
                foundedEntity.toArray()
        ));
    }

    @Test
    @DisplayName("getById(UUID id) - Should return RoleEntity entity by Id")
    void getById_shouldReturnRoleEntityById() {
        RoleEntity entity = RoleEntity.create("USER");

        UUID id = entity.getId();

        when(roleEntityRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<RoleEntity> foundedEntity = rolePostgresDatabase.getById(id);

        assertEquals(entity, foundedEntity.get());
    }

    @Test
    @DisplayName("getById(UUID id) - Should return Optional.empty() by Id")
    void getById_shouldReturnEmptyByIdWhenNotPresent() {
        UUID id = UuidGeneratorUtil.generateNewUuidV7();

        when(roleEntityRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RoleEntity> foundedEntity = rolePostgresDatabase.getById(id);

        assertEquals(Optional.empty(), foundedEntity);
    }

    @Test
    @DisplayName("getByName(String name) - Should return RoleEntity entity by name")
    void getByName_shouldReturnRoleEntityByName() {
        String name = "USER";
        RoleEntity entity = RoleEntity.create(name);

        when(roleEntityRepository.findByName(name)).thenReturn(Optional.of(entity));

        Optional<RoleEntity> foundedEntity = rolePostgresDatabase.getByName(name);

        assertFalse(foundedEntity.isEmpty());
        assertEquals(entity, foundedEntity.get());
    }

    @Test
    @DisplayName("getById(UUID id) - Should return Optional.empty() by name")
    void getByName_shouldReturnEmptyByNameWhenNotPresent() {
        String name = "USER";

        when(roleEntityRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<RoleEntity> foundedEntity = rolePostgresDatabase.getByName(name);

        assertEquals(Optional.empty(), foundedEntity);
    }

    @Test
    @DisplayName("getReferenceById(String name) - Should return RoleEntity entity reference by Id")
    void getReferenceById_shouldReturnRoleReferenceById() {
        RoleEntity entity = RoleEntity.create("USER");

        UUID id = entity.getId();

        when(roleEntityRepository.getReferenceById(id)).thenReturn(entity);

        RoleEntity foundedEntity = rolePostgresDatabase.getReferenceById(id);

        assertEquals(entity, foundedEntity);
    }

    @Test
    @DisplayName("existsById(UUID id) - Should return true if entity exist by Id")
    void getReferenceById_shouldReturnTrueWhenExistsById() {
        UUID id = UuidGeneratorUtil.generateNewUuidV7();

        when(roleEntityRepository.existsById(id)).thenReturn(true);

        boolean isExist = rolePostgresDatabase.existsById(id);

        assertTrue(isExist);
    }

    @Test
    @DisplayName("existsById(UUID id) - Should return false if entity not exist by Id")
    void getReferenceById_shouldReturnFalseWhenNotExistById() {
        UUID id = UuidGeneratorUtil.generateNewUuidV7();

        when(roleEntityRepository.existsById(id)).thenReturn(false);

        boolean isExist = rolePostgresDatabase.existsById(id);

        assertFalse(isExist);
    }

    @Test
    @DisplayName("existsByName(String name) - Should return true if entity exists by name")
    void getReferenceByName_shouldReturnTrueWhenExistsById() {
        String name = "USER";

        when(roleEntityRepository.existsByName(name)).thenReturn(true);

        boolean isExist = rolePostgresDatabase.existsByName(name);

        assertTrue(isExist);
    }

    @Test
    @DisplayName("existsByName(String name) - Should return false if entity not exist by name")
    void getReferenceByName_shouldReturnFalseWhenNotExistById() {
        String name = "USER";

        when(roleEntityRepository.existsByName(name)).thenReturn(false);

        boolean isExist = rolePostgresDatabase.existsByName(name);

        assertFalse(isExist);
    }
}