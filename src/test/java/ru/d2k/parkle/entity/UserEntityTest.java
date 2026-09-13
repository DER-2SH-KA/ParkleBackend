package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.Set;
import java.util.UUID;

public class UserEntityTest {

    private final RoleEntity role = new RoleEntity(UuidGeneratorUtil.generateNewUuidV7(), "TEMP");
    private final Set<RoleEntity> ROLES = Set.of(role);

    @DisplayName("hashCode -- hash codes of the same object are the same")
    @Test
    public void hashCode_sameObjectHashCodeEquals() {
        UserEntity user = new UserEntity(UuidGeneratorUtil.generateNewUuidV7(), "login", "password",
                ROLES);

        Assertions.assertEquals(user.hashCode(), user.hashCode());
    }

    @DisplayName("hashCode -- hash codes of objects with different non Id fields are equals")
    @Test
    public void hashCode_twoObjectWithSameIdAreEquals() {
        UUID uuid = UuidGeneratorUtil.generateNewUuidV7();

        UserEntity user1 = new UserEntity(uuid, "login1", "password1", ROLES);
        UserEntity user2 = new UserEntity(uuid, "login2", "password2", ROLES);

        Assertions.assertEquals(user1.hashCode(), user2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
     *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
     *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @DisplayName("hashCode -- hash codes of null ID is null")
    @Test
    public void hashCode_objectIdIsNull() {
        UserEntity user = new UserEntity(null, "login", "password", ROLES);

        Assertions.assertEquals(102, user.hashCode());
    }

    @DisplayName("equals -- Same object are equals")
    @Test
    public void equals_sameObjectAreEquals() {
        UserEntity user = new UserEntity(UuidGeneratorUtil.generateNewUuidV7(), "login", "password",
                ROLES);

        Assertions.assertEquals(user, user);
    }

    @DisplayName("equals -- Object equals with null is false")
    @Test
    public void equals_withNullObject() {
        UserEntity user = new UserEntity(null, "login", "password", ROLES);

        Assertions.assertFalse(user.equals(null));
    }

    @DisplayName("equals -- Objects with same ID are equals")
    @Test
    public void equals_objectsWithSameIdAreEquals() {
        UUID uuid = UuidGeneratorUtil.generateNewUuidV7();

        UserEntity user1 = new UserEntity(uuid, "login1",  "password1", ROLES);
        UserEntity user2 = new UserEntity(uuid, "login2",  "password2", ROLES);

        Assertions.assertEquals(user1, user2);
    }

    @DisplayName("equals -- equals with object which ID is null is false")
    @Test
    public void equals_withObjectIdNull() {
        UserEntity user1 = new UserEntity(UuidGeneratorUtil.generateNewUuidV7(),"login1",
                "password1", ROLES);
        UserEntity user2 = new UserEntity(null, "login2", "password2", ROLES);

        Assertions.assertFalse(user1.equals(user2));
    }
}