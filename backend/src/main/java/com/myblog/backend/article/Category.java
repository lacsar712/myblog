package com.myblog.backend.article;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    private String id;
    private String name;
    private String slug;

    public Category() {
        this.id = UUID.randomUUID().toString();
    }

    public Category(String name, String slug) {
        this();
        this.name = name;
        this.slug = slug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
