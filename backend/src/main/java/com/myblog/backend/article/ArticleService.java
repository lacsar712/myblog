package com.myblog.backend.article;

import com.myblog.backend.article.dto.ArticleDto;
import com.myblog.backend.article.dto.ArticleUpsertRequest;
import com.myblog.backend.article.dto.CategoryDto;
import com.myblog.backend.article.dto.TagDto;
import com.myblog.backend.common.AppException;
import com.myblog.backend.common.PageResult;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

  private final ArticleRepository repo;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;

  public ArticleService(ArticleRepository repo, CategoryRepository categoryRepository, TagRepository tagRepository) {
    this.repo = repo;
    this.categoryRepository = categoryRepository;
    this.tagRepository = tagRepository;
  }

  @Transactional(readOnly = true)
  public PageResult<ArticleDto> listPublished(int page, int pageSize) {
    var p = repo.findByStatusOrderByPublishedAtDesc(ArticleStatus.published, PageRequest.of(page - 1, pageSize));
    return new PageResult<>(p.getContent().stream().map(this::toDto).toList(), page, pageSize, p.getTotalElements());
  }

  @Transactional(readOnly = true)
  public ArticleDto getPublicById(String id) {
    var a = repo.findById(id).orElseThrow(() -> AppException.notFound("文章不存在"));
    if (a.getStatus() != ArticleStatus.published) {
      throw AppException.notFound("文章不存在");
    }
    return toDto(a);
  }

  @Transactional(readOnly = true)
  public ArticleDto getPublicBySlug(String slug) {
    var a = repo.findBySlug(slug).orElseThrow(() -> AppException.notFound("文章不存在"));
    if (a.getStatus() != ArticleStatus.published) {
      throw AppException.notFound("文章不存在");
    }
    return toDto(a);
  }

  @Transactional(readOnly = true)
  public PageResult<ArticleDto> adminList(String keyword, ArticleStatus status, int page, int pageSize) {
    var pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
    var p = repo.search(keyword, status, pageable);
    return new PageResult<>(p.getContent().stream().map(this::toDto).toList(), page, pageSize, p.getTotalElements());
  }

  @Transactional(readOnly = true)
  public ArticleDto adminGetById(String id) {
    var a = repo.findById(id).orElseThrow(() -> AppException.notFound("文章不存在"));
    return toDto(a);
  }

  @Transactional
  public ArticleDto create(ArticleUpsertRequest req) {
    if (repo.existsBySlug(req.slug())) {
      throw AppException.conflict("slug 已存在");
    }

    var now = LocalDateTime.now();
    var a = new Article();
    a.setId(UUID.randomUUID().toString());
    a.setTitle(req.title());
    a.setSlug(req.slug());
    a.setSummary(req.summary());
    a.setContentMd(req.contentMd());
    a.setStatus(ArticleStatus.draft);

    if (req.categoryId() != null) {
      a.setCategory(categoryRepository.findById(req.categoryId()).orElse(null));
    }

    if (req.tagIds() != null && !req.tagIds().isEmpty()) {
      a.setTags(new HashSet<>(tagRepository.findAllById(req.tagIds())));
    }

    a.setPublishedAt(null);
    a.setCreatedAt(now);
    a.setUpdatedAt(now);
    return toDto(repo.save(a));
  }

  @Transactional
  public ArticleDto update(String id, ArticleUpsertRequest req) {
    var a = repo.findById(id).orElseThrow(() -> AppException.notFound("文章不存在"));
    if (!a.getSlug().equals(req.slug()) && repo.existsBySlug(req.slug())) {
      throw AppException.conflict("slug 已存在");
    }

    a.setTitle(req.title());
    a.setSlug(req.slug());
    a.setSummary(req.summary());
    a.setContentMd(req.contentMd());

    if (req.categoryId() != null) {
      a.setCategory(categoryRepository.findById(req.categoryId()).orElse(null));
    } else {
      a.setCategory(null);
    }

    if (req.tagIds() != null) {
      a.setTags(new HashSet<>(tagRepository.findAllById(req.tagIds())));
    } else {
      a.getTags().clear(); // Or keep existing? Usually updates replace.
    }

    a.setUpdatedAt(LocalDateTime.now());
    return toDto(repo.save(a));
  }

  @Transactional
  public ArticleDto publish(String id) {
    var a = repo.findById(id).orElseThrow(() -> AppException.notFound("文章不存在"));
    if (a.getStatus() != ArticleStatus.published) {
      a.setStatus(ArticleStatus.published);
      if (a.getPublishedAt() == null) {
        a.setPublishedAt(LocalDateTime.now());
      }
      a.setUpdatedAt(LocalDateTime.now());
      a = repo.save(a);
    }
    return toDto(a);
  }

  @Transactional
  public ArticleDto unpublish(String id) {
    var a = repo.findById(id).orElseThrow(() -> AppException.notFound("文章不存在"));
    if (a.getStatus() != ArticleStatus.draft) {
      a.setStatus(ArticleStatus.draft);
      a.setUpdatedAt(LocalDateTime.now());
      a = repo.save(a);
    }
    return toDto(a);
  }

  private ArticleDto toDto(Article a) {
    CategoryDto catDto = a.getCategory() == null ? null
        : new CategoryDto(
            a.getCategory().getId(),
            a.getCategory().getName(),
            a.getCategory().getSlug());

    List<TagDto> tagDtos = a.getTags().stream()
        .map(t -> new TagDto(t.getId(), t.getName()))
        .toList();

    return new ArticleDto(
        a.getId(),
        a.getTitle(),
        a.getSlug(),
        a.getSummary(),
        a.getContentMd(),
        a.getStatus(),
        catDto,
        tagDtos,
        a.getPublishedAt(),
        a.getCreatedAt(),
        a.getUpdatedAt());
  }
}
