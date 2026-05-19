package com.myblog.backend.article;

import com.myblog.backend.article.dto.ArticleDto;
import com.myblog.backend.common.ApiResponse;
import com.myblog.backend.common.PageResult;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class PublicArticleController {
  private final ArticleService service;

  public PublicArticleController(ArticleService service) {
    this.service = service;
  }

  @GetMapping
  public ApiResponse<PageResult<ArticleDto>> list(
    @RequestParam(defaultValue = "1") @Min(1) int page,
    @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
  ) {
    return ApiResponse.ok(service.listPublished(page, pageSize));
  }

  @GetMapping("/{id}")
  public ApiResponse<ArticleDto> getById(@PathVariable String id) {
    return ApiResponse.ok(service.getPublicById(id));
  }

  @GetMapping("/slug/{slug}")
  public ApiResponse<ArticleDto> getBySlug(@PathVariable String slug) {
    return ApiResponse.ok(service.getPublicBySlug(slug));
  }
}

