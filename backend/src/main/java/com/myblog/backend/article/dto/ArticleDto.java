package com.myblog.backend.article.dto;

import com.myblog.backend.article.ArticleStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ArticleDto(
    String id,
    String title,
    String slug,
    String summary,
    String contentMd,
    ArticleStatus status,
    CategoryDto category,
    List<TagDto> tags,
    LocalDateTime publishedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
