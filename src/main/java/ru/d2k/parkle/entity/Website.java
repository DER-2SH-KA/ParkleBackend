package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.net.URI;
import java.util.UUID;

/** Entity for website. **/
@Entity
@Table(
        name = "website",
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "hex_id")
        }
)

@Getter
@Setter
@ToString(of = {"id", "user", "hexColor", "title", "description", "url"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Website {
    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "hexColor", nullable = false, scale = 7)
    private String hexColor;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "url", nullable = false)
    private URI url;

    private Website(User user, String hexColor, String title, String description, URI url) {
        this.id = Uuid7Generator.generateNewUUID();
        this.user = user;
        this.hexColor = hexColor;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    /**
     * Constructor only for tests.
     * @param id ID.
     * @param user {@link User} user owner.
     * @param hexColor HEX color of first letter.
     * @param title title of website.
     * @param description description of website.
     * @param url URL of website in Internet.
     * **/
    Website(UUID id, User user, String hexColor, String title, String description, URI url) {
        this.id = id;
        this.user = user;
        this.hexColor = hexColor;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    /**
     * Fabric method for create new {@link Website} with generated ID by UUID generator.
     * @param user {@link User} user owner.
     * @param hexColor HEX color of first letter.
     * @param title title of website.
     * @param description description of website.
     * @param url URL of website in Internet.
     * @return Created {@link Website} object.
     * **/
    public static Website create(User user, String hexColor, String title, String description, URI url) {
        return new Website(user, hexColor, title, description, url);
    }
}
