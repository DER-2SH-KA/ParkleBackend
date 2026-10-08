package ru.d2k.parkle.dao.database.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.d2k.parkle.entity.RoleEntity;
import ru.d2k.parkle.repository.RoleEntityRepository;
import java.util.List;
import java.util.Optional;
import static ru.d2k.parkle.test.consts.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolePostgresDatabaseTest {

    private final List<RoleEntity> ROLES = List.of(ROLE_ENTITY);

    @Mock
    private RoleEntityRepository repository;

    @InjectMocks
    private RolePostgresDatabase database;

    // save.
    @Test
    void save_shouldReturnSavedEntity() {
        when(repository.save(ROLE_ENTITY)).thenReturn(ROLE_ENTITY);

        var saved = database.save(ROLE_ENTITY);

        verify(repository).save(ROLE_ENTITY);
        assertEquals(ROLE_ENTITY, saved);
    }

    // getAll.
    @Test
    void getAll_shouldReturnAllEntities() {
        when(repository.findAll()).thenReturn(ROLES);

        var entities = database.getAll();

        verify(repository).findAll();
        assertArrayEquals(ROLES.toArray(), entities.toArray());
    }

    @Test
    void getAll_shouldReturnEmptyListWhenEntitiesNotPersisted() {
        when(repository.findAll()).thenReturn(List.of());

        var entities = database.getAll();

        verify(repository).findAll();
        assertArrayEquals(new RoleEntity[] { }, entities.toArray());
    }

    // getById.
    @Test
    void getById_shouldReturnEntityById() {
        when(repository.findById(ROLE_ID)).thenReturn(Optional.of(ROLE_ENTITY));

        var entity = database.getById(ROLE_ID);

        verify(repository).findById(ROLE_ID);
        assertEquals(Optional.of(ROLE_ENTITY), entity);
    }

    @Test
    void getById_shouldReturnEmptyOptionalByIdWhenEntityNotPersist() {
        when(repository.findById(ROLE_ID)).thenReturn(Optional.empty());

        var entity = database.getById(ROLE_ID);

        verify(repository).findById(ROLE_ID);
        assertEquals(Optional.empty(), entity);
    }

    // getByName.
    @Test
    void getByName_shouldReturnEntityByName() {
        when(repository.findByName(ROLE_NAME)).thenReturn(Optional.of(ROLE_ENTITY));

        var entity = database.getByName(ROLE_NAME);

        verify(repository).findByName(ROLE_NAME);
        assertEquals(Optional.of(ROLE_ENTITY), entity);
    }

    @Test
    void getByName_shouldReturnEmptyOptionalByNameWhenEntityNotPersist() {
        when(repository.findByName(ROLE_NAME)).thenReturn(Optional.empty());

        var entity = database.getByName(ROLE_NAME);

        verify(repository).findByName(ROLE_NAME);
        assertEquals(Optional.empty(), entity);
    }

    // getReferenceById.
    @Test
    void getReferenceById_shouldReturnEntityReferenceById() {
        when(repository.getReferenceById(ROLE_ID)).thenReturn(ROLE_ENTITY);

        var entity = database.getReferenceById(ROLE_ID);

        verify(repository).getReferenceById(ROLE_ID);
        assertEquals(ROLE_ENTITY, entity);
    }

    // deleteById.
    @Test
    void deleteById_shouldVerifyThatDeleteByIdWacExecutedOnce() {
        doNothing().when(repository).deleteById(ROLE_ID);

        database.deleteById(ROLE_ID);

        verify(repository).deleteById(ROLE_ID);
    }

    // existsById.
    @Test
    void existsById_shouldReturnTrueWhenEntityExistsById() {
        when(repository.existsById(ROLE_ID)).thenReturn(true);

        boolean isExist = database.existsById(ROLE_ID);

        verify(repository).existsById(ROLE_ID);
        assertTrue(isExist);
    }

    @Test
    void existsById_shouldReturnFalseWhenEntityNotExistById() {
        when(repository.existsById(ROLE_ID)).thenReturn(false);

        boolean isExist = database.existsById(ROLE_ID);

        verify(repository).existsById(ROLE_ID);
        assertFalse(isExist);
    }

    // existsByName.
    @Test
    void existsByName_shouldReturnTrueWhenEntityExistsByName() {
        when(repository.existsByName(ROLE_NAME)).thenReturn(true);

        boolean isExist = database.existsByName(ROLE_NAME);

        verify(repository).existsByName(ROLE_NAME);
        assertTrue(isExist);
    }

    @Test
    void existsByName_shouldReturnFalseWhenEntityNotExistByName() {
        when(repository.existsByName(ROLE_NAME)).thenReturn(false);

        boolean isExist = database.existsByName(ROLE_NAME);

        verify(repository).existsByName(ROLE_NAME);
        assertFalse(isExist);
    }
}