package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.net.URI;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "user", "hexColor", "title", "description", "url"})
@Entity
@Table(
        name = "website",
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "hex_id")
        }
)
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hex_id")
    private HexColor hexColor;

    @Column(
            name = "title",
            nullable = false,
            length = 100
    )
    private String title;

    @Setter
    @Column(
            name = "description",
            length = 255
    )
    private String description;

    @Column(
            name = "url",
            nullable = false
    )
    private URI url;

    public Website(User user, HexColor hex, String title, String description, URI url) {
        this.setUser(user);
        this.setHexColor(hex);
        this.setTitle(title);
        this.setDescription(description);
        this.setUrl(url);
    }

    Website(Long id, User user, HexColor hex, String title, String description, URI url) {
        this.id = id;
        this.setUser(user);
        this.setHexColor(hex);
        this.setTitle(title);
        this.setDescription(description);
        this.setUrl(url);
    }

    /**
     * Set new user for {@code Website} entity.
     * @param newUser new {@code User}.
     * @throws IllegalArgumentException when {@code newUser} is NULL.
     * **/
    public void setUser(User newUser) throws IllegalArgumentException {
        if (Objects.nonNull(newUser)) {
            this.user = newUser;
        }
        else {
            throw  new IllegalArgumentException("New User for Website is NULL");
        }
    }

    /**
     * Set new hex color for {@code Website} entity.
     * @param newHexColor new {@code HexColor}.
     * @throws IllegalArgumentException when {@code newHexColor} is NULL.
     * **/
    public void setHexColor(HexColor newHexColor) throws IllegalArgumentException {
        if (Objects.nonNull(newHexColor)) {
            this.hexColor = newHexColor;
        }
        else {
            throw  new IllegalArgumentException("New HexColor for Website is NULL");
        }
    }

    /**
     * Set new title for {@code Website} entity.
     * @param newTitle new title.
     * @throws IllegalArgumentException when {@code newTitle} is NULL.
     * **/
    public void setTitle(String newTitle) throws IllegalArgumentException {
        if (Objects.nonNull(newTitle) && !newTitle.isBlank()) {
            this.title = newTitle;
        }
        else {
            throw  new IllegalArgumentException("New title for Website is NULL or Blank");
        }
    }

    /**
     * Set new uri for {@code Website} entity.
     * @param newUri new uri.
     * @throws IllegalArgumentException when {@code newUri} is NULL.
     * **/
    public void setUrl(URI newUri) throws IllegalArgumentException {
        if (Objects.nonNull(newUri)) {
            this.url = newUri;
        }
        else {
            throw  new IllegalArgumentException("New uri for Website is NULL or Blank");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null) return false;

        if (this == o) return true;

        if (!(o instanceof Website oWebsite)) return false;

        return Objects.nonNull(this.id) &&
                Objects.nonNull(oWebsite.id) &&
                Objects.equals(this.id, oWebsite.id);
    }

    @Override
    public final int hashCode() {
        return Objects.nonNull(this.id) ?
                Objects.hashCode(this.id) : 31;
    }
}
