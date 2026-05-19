package com.myblog.backend.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String> {
  boolean existsBySlug(String slug);

  Optional<Article> findBySlug(String slug);

  Page<Article> findByStatusOrderByPublishedAtDesc(ArticleStatus status, Pageable pageable);

  @Query("""
    select a from Article a
    where (:status is null or a.status = :status)
      and (
        :keyword is null or :keyword = ''
        or lower(a.title) like lower(concat('%', :keyword, '%'))
        or lower(a.summary) like lower(concat('%', :keyword, '%'))
      )
    order by a.updatedAt desc
  """)
  Page<Article> search(@Param("keyword") String keyword, @Param("status") ArticleStatus status, Pageable pageable);
}

