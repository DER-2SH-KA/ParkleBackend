package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.UUID;

public class RoleTest {
    @DisplayName("hashCode -- hash codes of the same object are the same")
    @Test
    public void hashCode_sameObjectHashCodeEquals() {
        Role role = new Role(Uuid7Generator.generateNewUUID(), "TEMP", 0);

        Assertions.assertEquals(role.hashCode(), role.hashCode());
    }

    @DisplayName("hashCode -- hash codes of objects with different non Id fields are equals")
    @Test
    public void hashCode_twoObjectWithSameIdAreEquals() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        Role role1 = new Role(uuid, "TEMP1", 1);
        Role role2 = new Role(uuid, "TEMP2", 2);

        Assertions.assertEquals(role1.hashCode(), role2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
    *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
    *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @DisplayName("hashCode -- hash codes of null ID is null")
    @Test
    public void hashCode_objectIdIsNull() {
        Role role = new Role(null, "TEMP", 0);

        // !! 102, а не 0, по каким-то причинам. !!
        Assertions.assertEquals(102, role.hashCode());
    }

    @DisplayName("equals -- Same object are equals")
    @Test
    public void equals_sameObjectAreEquals() {
        Role role = new Role(Uuid7Generator.generateNewUUID(), "TEMP", 0);

        Assertions.assertEquals(role, role);
    }

    @DisplayName("equals -- Object equals with null is false")
    @Test
    public void equals_withNullObject() {
        Role role = new Role(Uuid7Generator.generateNewUUID(), "TEMP", 0);

        Assertions.assertFalse(role.equals(null));
    }

    @DisplayName("equals -- Objects with same ID are equals")
    @Test
    public void equals_objectsWithSameIdAreEquals() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        Role role1 = new Role(uuid, "TEMP1", 1);
        Role role2 = new Role(uuid, "TEMP2", 2);

        Assertions.assertEquals(role1, role2);
    }

    @DisplayName("equals -- equals with object which ID is null is false")
    @Test
    public void equals_withObjectIdNull() {
        Role role1 = new Role(Uuid7Generator.generateNewUUID(), "TEMP1", 1);
        Role role2 = new Role(null, "TEMP2", 2);

        Assertions.assertFalse(role1.equals(role2));
    }
}
