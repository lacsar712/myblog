SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE myblog;

CREATE TABLE IF NOT EXISTS categories (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  slug VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tags (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS articles (
  id VARCHAR(36) PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  slug VARCHAR(200) NOT NULL,
  summary VARCHAR(500) NOT NULL,
  content_md LONGTEXT NOT NULL,
  status VARCHAR(20) NOT NULL,
  category_id VARCHAR(36) NULL,
  published_at DATETIME NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS article_tags (
  article_id VARCHAR(36) NOT NULL,
  tag_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (article_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_articles_slug ON articles (slug);
CREATE INDEX idx_articles_status_published_at ON articles (status, published_at);
CREATE INDEX idx_articles_updated_at ON articles (updated_at);
CREATE INDEX idx_articles_category ON articles (category_id);

SET FOREIGN_KEY_CHECKS = 1;

