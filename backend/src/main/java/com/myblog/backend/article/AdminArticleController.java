package com.myblog.backend.article;

import com.myblog.backend.article.dto.ArticleDto;
import com.myblog.backend.article.dto.ArticleUpsertRequest;
import com.myblog.backend.common.ApiResponse;
import com.myblog.backend.common.PageResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {
  private final ArticleService service;

  public AdminArticleController(ArticleService service) {
    this.service = service;
  }

  @GetMapping
  public ApiResponse<PageResult<ArticleDto>> list(
    @RequestParam(required = false) String keyword,
    @RequestParam(required = false) ArticleStatus status,
    @RequestParam(defaultValue = "1") @Min(1) int page,
    @RequestParam(defaultValue = "10") @Min(1) @Max(50) int pageSize
  ) {
    return ApiResponse.ok(service.adminList(keyword, status, page, pageSize));
  }

  @GetMapping("/{id}")
  public ApiResponse<ArticleDto> get(@PathVariable String id) {
    return ApiResponse.ok(service.adminGetById(id));
  }

  @PostMapping
  public ApiResponse<ArticleDto> create(@Valid @RequestBody ArticleUpsertRequest req) {
    return ApiResponse.ok(service.create(req));
  }

  @PutMapping("/{id}")
  public ApiResponse<ArticleDto> update(@PathVariable String id, @Valid @RequestBody ArticleUpsertRequest req) {
    return ApiResponse.ok(service.update(id, req));
  }

  @PostMapping("/{id}/publish")
  public ApiResponse<ArticleDto> publish(@PathVariable String id) {
    return ApiResponse.ok(service.publish(id));
  }

  @PostMapping("/{id}/unpublish")
  public ApiResponse<ArticleDto> unpublish(@PathVariable String id) {
    return ApiResponse.ok(service.unpublish(id));
  }
}

