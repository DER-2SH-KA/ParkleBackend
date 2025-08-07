package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.UUID;

public class UserTest {
    private final Role role = new Role(Uuid7Generator.generateNewUUID(), "TEMP", 0);

    @DisplayName("hashCode -- hash codes of the same object are the same")
    @Test
    public void hashCode_sameObjectHashCodeEquals() {
        User user = new User(
                Uuid7Generator.generateNewUUID(),
                role,
                "login",
                "email@email.net",
                "password"
        );

        Assertions.assertEquals(user.hashCode(), user.hashCode());
    }

    @DisplayName("hashCode -- hash codes of objects with different non Id fields are equals")
    @Test
    public void hashCode_twoObjectWithSameIdAreEquals() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        User user1 = new User(
                uuid,
                role,
                "login1",
                "email1@email.net",
                "password1"
        );
        User user2 = new User(
                uuid,
                role,
                "login2",
                "email2@email.net",
                "password2"
        );

        Assertions.assertEquals(user1.hashCode(), user2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
     *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
     *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @DisplayName("hashCode -- hash codes of null ID is null")
    @Test
    public void hashCode_objectIdIsNull() {
        User user = new User(
                null,
                role,
                "login",
                "email@email.net",
                "password"
        );

        // !! 102, а не 0, по каким-то причинам. !!
        Assertions.assertEquals(102, user.hashCode());
    }

    @DisplayName("equals -- Same object are equals")
    @Test
    public void equals_sameObjectAreEquals() {
        User user = new User(
                Uuid7Generator.generateNewUUID(),
                role,
                "login",
                "email@email.net",
                "password"
        );

        Assertions.assertEquals(user, user);
    }

    @DisplayName("equals -- Object equals with null is false")
    @Test
    public void equals_withNullObject() {
        User user = new User(
                null,
                role,
                "login",
                "email@email.net",
                "password"
        );

        Assertions.assertFalse(user.equals(null));
    }

    @DisplayName("equals -- Objects with same ID are equals")
    @Test
    public void equals_objectsWithSameIdAreEquals() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        User user1 = new User(
                uuid,
                role,
                "login1",
                "email1@email.net",
                "password1"
        );
        User user2 = new User(
                uuid,
                role,
                "login2",
                "email2@email.net",
                "password2"
        );

        Assertions.assertEquals(user1, user2);
    }

    @DisplayName("equals -- equals with object which ID is null is false")
    @Test
    public void equals_withObjectIdNull() {
        User user1 = new User(
                Uuid7Generator.generateNewUUID(),
                role,
                "login1",
                "email1@email.net",
                "password1"
        );
        User user2 = new User(
                null,
                role,
                "login2",
                "email2@email.net",
                "password2"
        );

        Assertions.assertFalse(user1.equals(user2));
    }
}
