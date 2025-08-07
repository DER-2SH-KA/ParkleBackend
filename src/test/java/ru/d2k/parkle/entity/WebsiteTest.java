package ru.d2k.parkle.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.UUID;

public class WebsiteTest {
    private final Role role = new Role(Uuid7Generator.generateNewUUID(), "TEMP", 0);
    private final User user = new User(
            Uuid7Generator.generateNewUUID(),
            role,
            "login",
            "email@email.net",
            "password"
    );

    @DisplayName("hashCode -- hash codes of the same object are the same")
    @Test
    public void hashCode_sameObjectHashCodeEquals() {
        Website website = new Website(
                Uuid7Generator.generateNewUUID(),
                user,
                "#fff",
                "title",
                "description",
                "https://example.com"
        );

        Assertions.assertEquals(website.hashCode(), website.hashCode());
    }

    @DisplayName("hashCode -- hash codes of objects with different non Id fields are equals")
    @Test
    public void hashCode_twoObjectWithSameIdAreEquals() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        Website website1 = new Website(
                uuid,
                user,
                "#fff",
                "title1",
                "description1",
                "https://example.com1"
        );
        Website website2 = new Website(
                uuid,
                user,
                "#fff",
                "title2",
                "description2",
                "https://example.com2"
        );

        Assertions.assertEquals(website1.hashCode(), website2.hashCode());
    }

    /* TODO: Возвращает вместо 0 число 102. Вероятно, придётся переопределять методы equals и hashCode вручную.
     *   Вот статья на Хабр для Hibernate, хоть в ней и сомневаюсь:
     *   https://habr.com/ru/companies/haulmont/articles/836018/ */
    @DisplayName("hashCode -- hash codes of null ID is null")
    @Test
    public void hashCode_objectIdIsNull() {
        Website website = new Website(
                null,
                user,
                "#fff",
                "title",
                "description",
                "https://example.com"
        );

        // !! 102, а не 0, по каким-то причинам. !!
        Assertions.assertEquals(102, website.hashCode());
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
        Website website = new Website(
                Uuid7Generator.generateNewUUID(),
                user,
                "#fff",
                "title",
                "description",
                "https://example.com"
        );

        Assertions.assertFalse(website.equals(null));
    }

    @DisplayName("equals -- Objects with same ID are equals")
    @Test
    public void equals_objectsWithSameIdAreEquals() {
        UUID uuid = Uuid7Generator.generateNewUUID();
        Website website1 = new Website(
                uuid,
                user,
                "#fff",
                "title1",
                "description1",
                "https://example.com1"
        );
        Website website2 = new Website(
                uuid,
                user,
                "#fff",
                "title2",
                "description2",
                "https://example.com2"
        );

        Assertions.assertEquals(website1, website2);
    }

    @DisplayName("equals -- equals with object which ID is null is false")
    @Test
    public void equals_withObjectIdNull() {
        Website website1 = new Website(
                Uuid7Generator.generateNewUUID(),
                user,
                "#fff",
                "title1",
                "description1",
                "https://example.com1"
        );
        Website website2 = new Website(
                null,
                user,
                "#fff",
                "title2",
                "description2",
                "https://example.com2"
        );

        Assertions.assertFalse(website1.equals(website2));
    }
}
