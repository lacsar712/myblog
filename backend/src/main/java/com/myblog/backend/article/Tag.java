package com.myblog.backend.article;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    private String id;
    private String name;

    public Tag() {
        this.id = UUID.randomUUID().toString();
    }

    public Tag(String name) {
        this();
        this.name = name;
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
}
