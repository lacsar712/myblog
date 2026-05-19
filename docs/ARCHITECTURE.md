# 项目架构说明

## 技术栈
- **前端**: Vue 3 (Composition API), Vite, Tailwind CSS, Ant Design Vue
- **后端**: Java 17, Spring Boot 3.x, Spring Data JPA
- **数据库**: MySQL 8.0
- **部署**: Docker, Docker Compose, Nginx

## 逻辑架构
项目采用前后端分离架构：
1. **用户界面层 (Frontend)**: 基于 Vue 3 构建的响应式 Web 应用，通过 Nginx 托管。
2. **API 网关层 (Nginx)**: 负责静态资源分发及后端 API 请求转发（`/api` 路径代理）。
3. **业务逻辑层 (Backend)**: 基于 Spring Boot 的 RESTful 服务，包含文章管理、分类管理、标签管理等核心模块。
4. **数据持久层 (MySQL)**: 存储用户信息、文章内容、分类及标签等结构化数据。

## 部署拓扑
通过 `docker-compose` 编排三个容器：
- `db`: MySQL 数据库容器。
- `backend`: Java Spring Boot 服务容器，依赖 `db`。
- `frontend`: Nginx + 前端静态资源容器，通过代理连接 `backend`。
