USE myblog;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE article_tags;
TRUNCATE TABLE tags;
TRUNCATE TABLE categories;
TRUNCATE TABLE articles;

-- Categories
INSERT INTO categories (id, name, slug) VALUES 
('c1', '前端开发', 'frontend'),
('c2', '后端架构', 'backend'),
('c3', '生活随笔', 'life'),
('c4', '人工智能', 'ai');

-- Tags
INSERT INTO tags (id, name) VALUES 
('t1', 'Vue3'),
('t2', 'Spring Boot'),
('t3', 'Java'),
('t4', 'TailwindCSS'),
('t5', 'Docker'),
('t6', 'ChatGPT'),
('t7', '感悟');

-- Articles
INSERT INTO articles (id, title, slug, summary, content_md, status, category_id, published_at, created_at, updated_at) VALUES 
('a1', 'Vue 3 组合式 API 最佳实践', 'vue3-composition-api-best-practices', '本文详细介绍了 Vue 3 Composition API 的核心概念和实际应用中的最佳实践方式。', 
'# Vue 3 组合式 API 最佳实践

## 引言

Vue 3 引入了 **Composition API**，这是一个革命性的特性，它改变了我们组织 Vue 组件逻辑的方式。

### 为什么选择 Composition API?

1. **更好的逻辑复用**: 相比 Mixins，Composables 更加清晰和易于维护。
2. **更好的类型推导**: 对 TypeScript 的支持更加友好。
3. **更灵活的代码组织**: 可以根据功能而非选项来组织代码。

```javascript
import { ref, onMounted } from "vue";

export function useUser() {
  const user = ref(null);
  
  onMounted(() => {
    // fetch user
  });

  return { user };
}
```

## 结论

掌握 Composition API 是每个 Vue 开发者的必修课。', 
'published', 'c1', NOW(), NOW(), NOW()),

('a2', 'Spring Boot 3.0 新特性解析', 'spring-boot-3-features', '深入解析 Spring Boot 3.0 带来的重大更新，包括对 Java 17 的支持和 AOT 编译。', 
'# Spring Boot 3.0 新特性解析

Spring Boot 3.0 是该框架的一个重要里程碑。

## 核心变化

- **Java 17 基准**: 最低要求 Java 17，充分利用新语言特性。
- **Jakarta EE 9**: 从 `javax.*` 迁移到 `jakarta.*`。
- **Native Image 支持**: 基于 GraalVM 的原生镜像支持，大幅缩短启动时间。

> 注意：升级过程可能涉及到大量的包名替换。

## 性能提升

通过 AOT 编译，应用启动速度可以达到毫秒级。', 
'published', 'c2', NOW(), NOW(), NOW()),

('a3', 'Docker 容器化部署指南', 'docker-deployment-guide', '从零开始学习 Docker 容器化部署，编写 Dockerfile 和 docker-compose.yml。', 
'# Docker 容器化部署指南

容器化是现代微服务架构的基础。

## 编写 Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 使用 Docker Compose

对于多服务应用，Docker Compose 是不可或缺的工具。', 
'published', 'c2', NOW(), NOW(), NOW()),

('a4', '我的 2025 年终总结', 'my-2025-summary', '回顾过去一年在技术和生活上的收获与遗憾，展望 2026。', 
'# 我的 2025 年终总结

时间如白驹过隙，转眼间 2025 年已经过去。

## 技术成长

今年主要深入学习了 Rust 和 WebAssembly。

## 生活点滴

- 去了两趟旅行。
- 坚持每周跑步。

## 2026 展望

希望能在开源社区有更多的贡献。', 
'published', 'c3', NOW(), NOW(), NOW()),

('a5', 'AI 编程助手：是助手还是替代者？', 'ai-coding-assistant', '探讨 AI 在编程领域的角色，如何利用 AI 提高效率而不是产生依赖。', 
'# AI 编程助手：是助手还是替代者？

随着 ChatGPT 和 Copilot 的普及，在此讨论 AI 对程序员的影响。

## 效率提升

AI 可以帮助我们要编写样板代码，生成测试用例。

## 潜在风险

过度依赖可能导致基础能力退化。

**结论**：拥抱变化，但保持核心竞争力。', 
'draft', 'c4', NULL, NOW(), NOW());

-- Article Tags Relations
INSERT INTO article_tags (article_id, tag_id) VALUES 
('a1', 't1'), ('a1', 't4'),
('a2', 't2'), ('a2', 't3'),
('a3', 't5'), ('a3', 't2'),
('a4', 't7'),
('a5', 't6');

SET FOREIGN_KEY_CHECKS = 1;
