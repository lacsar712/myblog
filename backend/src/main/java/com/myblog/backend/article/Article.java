package com.myblog.backend.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article {
  @Id
  @Column(name = "id", length = 36)
  private String id;

  @Column(name = "title", length = 200, nullable = false)
  private String title;

  @Column(name = "slug", length = 200, nullable = false)
  private String slug;

  @Column(name = "summary", length = 500, nullable = false)
  private String summary;

  @Column(name = "content_md", columnDefinition = "LONGTEXT", nullable = false)
  private String contentMd;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20, nullable = false)
  private ArticleStatus status;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToMany
  @JoinTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  @Column(name = "published_at")
  private LocalDateTime publishedAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public Article() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getContentMd() {
    return contentMd;
  }

  public void setContentMd(String contentMd) {
    this.contentMd = contentMd;
  }

  public ArticleStatus getStatus() {
    return status;
  }

  public void setStatus(ArticleStatus status) {
    this.status = status;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public LocalDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(LocalDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
