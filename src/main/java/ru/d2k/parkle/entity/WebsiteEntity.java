package ru.d2k.parkle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "websites", indexes = @Index(columnList = "user_id"))
@Getter
@Setter
@ToString(of = {"id", "user", "hexColor", "title", "description", "url"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebsiteEntity {

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "hexColor", nullable = false, length = 7)
    private String hexColor;

    @Column(name = "title", nullable = false, length = 40)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "website_and_tags", joinColumns = @JoinColumn(name = "website_id"),
            inverseJoinColumns = @JoinColumn(name = "website_tag_id"))
    private List<WebsiteTagEntity> tags = new ArrayList<>();

    private WebsiteEntity(UserEntity user, String hexColor, String title, String description, String url,
                          List<WebsiteTagEntity> tags) {
        this.id = UuidGeneratorUtil.generateNewUuidV7();
        this.user = user;
        this.hexColor = hexColor;
        this.title = title;
        this.description = description;
        this.url = url;
        this.tags = tags;
    }

    WebsiteEntity(UUID id, UserEntity user, String hexColor, String title, String description, String url,
                  List<WebsiteTagEntity> tags) {
        this.id = id;
        this.user = user;
        this.hexColor = hexColor;
        this.title = title;
        this.description = description;
        this.url = url;
        this.tags = tags;
    }

    public static WebsiteEntity create(UserEntity user, String hexColor, String title, String description, String url,
                                       List<WebsiteTagEntity> tags) {
        return new WebsiteEntity(user, hexColor, title, description, url, tags);
    }
}