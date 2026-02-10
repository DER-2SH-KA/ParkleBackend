package ru.d2k.parkle.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.d2k.parkle.dao.cache.role.RoleCacheSource;
import ru.d2k.parkle.dao.database.role.RoleDatabaseSource;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.redis.RedisCacheKeys;
import ru.d2k.parkle.utils.generator.Uuid7Generator;
import ru.d2k.parkle.utils.mapper.RoleMapper;

import java.time.Duration;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class RoleDaoTest {
    @Mock private RoleDatabaseSource roleDatabase;
    @Mock private RoleCacheSource roleCache;
    @Mock private RoleMapper roleMapper;

    @InjectMocks private RoleDao roleDao;

    private final UUID ROLE_ID = Uuid7Generator.generateNewUUID();
    private final String ROLE_NAME = "USER";
    private final Integer ROLE_PRIORITY = 10;

    @Test
    @DisplayName("create(RoleCreateDto cdto) - Should return correct RoleCache")
    void create_shouldReturnCorrectRoleCache() {
        RoleCreateDto cdto = new RoleCreateDto(ROLE_NAME, ROLE_PRIORITY);

        Role entity = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);
        RoleCache expectedCache = new RoleCache(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);

        try (MockedStatic<Role> roleMock = mockStatic(Role.class)) {

            // Create entity from RoleCreateDto object.
            roleMock.when(() -> Role.create(ROLE_NAME, ROLE_PRIORITY))
                    .thenReturn(entity);

            // Save entity to database.
            when(roleDatabase.save(entity))
                    .thenReturn(entity);

            // Convert from entity to cache object.
            when(roleMapper.toCache(entity))
                    .thenReturn(expectedCache);

            // Set cache object of saved entity to cache.
            doNothing().when(roleCache)
                    .set(
                            RedisCacheKeys.ROLE_SLICE_KEY + expectedCache.name(),
                            expectedCache,
                            Duration.ofMinutes(15)
                    );

            // Test method.
            RoleCache actualCache = roleDao.create(cdto);

            assertEquals(expectedCache, actualCache);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("getAll() - Should return set of RoleCache objects")
    void getAll_shouldReturnSetOfRoleCache() {
        Role role1 = Role.create("USER1", 10);
        Role role2 = Role.create("USER2", 10);

        RoleCache cache1 = new RoleCache(role1.getId(), role1.getName(), role1.getPriority());
        RoleCache cache2 = new RoleCache(role2.getId(), role2.getName(), role2.getPriority());

        List<Role> entitiesToFindAll = List.of(role1, role2);
        Set<RoleCache> expectedCaches = Set.of(cache1, cache2);

        // Find all entities from database.
        when(roleDatabase.getAll())
                .thenReturn(entitiesToFindAll);

        // Convert entities to caches.
        when(roleMapper.toCache(role1))
                .thenReturn(cache1);
        when(roleMapper.toCache(role2))
                .thenReturn(cache2);

        // Test method.
        Set<RoleCache> actualCaches = roleDao.getAll();

        assertTrue(
                actualCaches.containsAll(expectedCaches) &&
                        actualCaches.size() == expectedCaches.size()
        );
    }

    @Test
    @DisplayName("getById(UUID id) - Should return list of RoleCache objects by ID")
    void getById_shouldReturnRoleCacheById() {
        Role entity = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);
        Optional<RoleCache> expectedCache = Optional.of(
                new RoleCache(ROLE_ID, ROLE_NAME, ROLE_PRIORITY)
        );

        // Get entity from database by ID.
        when(roleDatabase.getById(ROLE_ID))
                .thenReturn(Optional.of(entity));

        // Convert from entity to cache.
        when(roleMapper.toCache(entity))
                .thenReturn(expectedCache.get());

        // Test method.
        Optional<RoleCache> actualCache = roleDao.getById(ROLE_ID);

        assertEquals(expectedCache, actualCache);
    }

    @Test
    @DisplayName("getByName(String name) - Should return RoleCache objects by name from cache")
    void getByName_shouldReturnRoleCacheByNameFromCache() {
        Role entity = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);
        Optional<RoleCache> expectedCache = Optional.of(
                new RoleCache(ROLE_ID, ROLE_NAME, ROLE_PRIORITY)
        );

        // Get cache object from cache.
        when(roleCache.get(RedisCacheKeys.ROLE_SLICE_KEY + entity.getName()))
                .thenReturn(expectedCache);

        // Test method.
        Optional<RoleCache> actualCache = roleDao.getByName(ROLE_NAME);

        assertEquals(expectedCache, actualCache);
    }

    @Test
    @DisplayName("getByName(String name) - Should return RoleCache objects by name from database")
    void getByName_shouldReturnRoleCacheByNameFromDatabase() {
        Role entity = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);
        Optional<RoleCache> expectedCache = Optional.of(
                new RoleCache(ROLE_ID, ROLE_NAME, ROLE_PRIORITY)
        );

        // Get empty from cache.
        when(roleCache.get(RedisCacheKeys.ROLE_SLICE_KEY + entity.getName()))
                .thenReturn(Optional.empty());

        // Get entity from database.
        when(roleDatabase.getByName(entity.getName()))
                .thenReturn(Optional.of(entity));

        // Convert entity from database to cache.
        when(roleMapper.toCache(entity))
                .thenReturn(expectedCache.get());

        // Set to cache.
        doNothing().when(roleCache)
                .set(
                        RedisCacheKeys.ROLE_SLICE_KEY + expectedCache.get().name(),
                        expectedCache.get(),
                        Duration.ofMinutes(15)
                );

        // Test method.
        Optional<RoleCache> actualCache = roleDao.getByName(ROLE_NAME);

        assertEquals(expectedCache, actualCache);
    }

    @Test
    @DisplayName("getByName(String name) - Should return empty by name")
    void getByName_shouldReturnEmptyByName() {
        Role entity = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);
        Optional<RoleCache> expectedCache = Optional.of(
                new RoleCache(ROLE_ID, ROLE_NAME, ROLE_PRIORITY)
        );

        // Get empty from cache.
        when(roleCache.get(RedisCacheKeys.ROLE_SLICE_KEY + entity.getName()))
                .thenReturn(Optional.empty());

        // Get entity from database.
        when(roleDatabase.getByName(entity.getName()))
                .thenReturn(Optional.empty());

        // Test method.
        Optional<RoleCache> actualCache = roleDao.getByName(ROLE_NAME);

        assertTrue(actualCache.isEmpty());
    }

    @Test
    @DisplayName("update(UUID id, RoleUpdateDto udto) - Should return updated RoleCache when entity is present")
    void update_shouldReturnUpdatedRoleCacheWhenPresent() {
        RoleUpdateDto udto = new RoleUpdateDto(ROLE_ID, "USER1", ROLE_PRIORITY);

        Role entityOld = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);
        Role entityUpdated = new Role(udto.id(), udto.name(), udto.priority());

        Optional<RoleCache> cache = Optional.of(
                new RoleCache(ROLE_ID, ROLE_NAME, ROLE_PRIORITY)
        );
        Optional<RoleCache> expectedCache = Optional.of(
                new RoleCache(udto.id(), udto.name(), udto.priority())
        );

        // Get entity from database by ID.
        when(roleDatabase.getById(ROLE_ID))
                .thenReturn(Optional.of(entityOld));

        // Update entity by dto.
        when(roleMapper.updateEntityByDto(entityOld, udto))
                .thenReturn(entityUpdated);

        // Update entity in database.
        when(roleDatabase.save(entityUpdated))
                .thenReturn(entityUpdated);

        // Convert from entity to cache.
        when(roleMapper.toCache(entityUpdated))
                .thenReturn(expectedCache.get());

        // Delete cache of old entity.
        doNothing().when(roleCache)
                .delete(RedisCacheKeys.ROLE_SLICE_KEY + entityOld.getName());

        // Set cache of updated entity.
        doNothing().when(roleCache)
                .set(
                        RedisCacheKeys.ROLE_SLICE_KEY + expectedCache.get().name(),
                        expectedCache.get(),
                        Duration.ofMinutes(15)
                );

        // Test method.
        Optional<RoleCache> actualCache = roleDao.update(ROLE_ID, udto);

        assertEquals(expectedCache, actualCache);
    }

    @Test
    @DisplayName("update(UUID id, RoleUpdateDto udto) - Should return empty when entity isn't present")
    void update_shouldReturnEmptyWhenNotPresent() {
        RoleUpdateDto udto = new RoleUpdateDto(ROLE_ID, "USER1", ROLE_PRIORITY);

        Role entityOld = new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY);

        // Get entity from database by ID.
        when(roleDatabase.getById(ROLE_ID))
                .thenReturn(Optional.empty());

        // Test method.
        Optional<RoleCache> actualCache = roleDao.update(ROLE_ID, udto);

        assertEquals(Optional.empty(), actualCache);
    }

    @Test
    @DisplayName("delete(UUID id) - Should return true when entity is present")
    void delete_shouldReturnTrueWhenPresent() {
        Optional<Role> entity = Optional.of(new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY));

        // Get entity from database by ID.
        when(roleDatabase.getById(ROLE_ID))
                .thenReturn(entity);

        // Delete from cache.
        doNothing().when(roleCache)
                .delete(RedisCacheKeys.ROLE_SLICE_KEY + entity.get().getName());

        // Delete from database.
        doNothing().when(roleDatabase)
                .deleteById(ROLE_ID);

        // Check is exist in database now.
        when(roleDatabase.existsById(ROLE_ID))
                .thenReturn(false);

        // Test method.
        boolean actualResult = roleDao.deleteById(ROLE_ID);

        assertTrue(actualResult);
    }

    @Test
    @DisplayName("delete(UUID id) - Should return true when entity isn't present")
    void delete_shouldReturnTrueWhenNotPresent() {
        Optional<Role> entity = Optional.of(new Role(ROLE_ID, ROLE_NAME, ROLE_PRIORITY));

        // Get entity from database by ID.
        when(roleDatabase.getById(ROLE_ID))
                .thenReturn(Optional.empty());

        // Test method.
        boolean actualResult = roleDao.deleteById(ROLE_ID);

        assertTrue(actualResult);
    }
}
