package com.myblog.backend.article.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ArticleUpsertRequest(
    @NotBlank String title,
    @NotBlank String slug,
    @NotBlank String summary,
    @NotBlank String contentMd,
    String categoryId,
    List<String> tagIds) {
}
