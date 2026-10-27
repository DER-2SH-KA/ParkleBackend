package ru.d2k.parkle.model.domain;

import java.util.List;
import java.util.UUID;

public record Website(UUID id, String title, String description, String url, String hexColor,
                      List<WebsiteTag> tags) { }