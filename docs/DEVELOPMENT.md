# 开发指南

## 环境依赖
- JDK 17
- Node.js 20+
- Docker & Docker Compose
- Maven 3.9+

## 本地启动

### 后端开发
1. 进入 `backend` 目录。
2. 修改 `application.yml` 中的数据库连接。
3. 执行 `mvn spring-boot:run`。

### 前端开发
1. 进入 `frontend` 目录。
2. 执行 `npm install` 安装依赖。
3. 执行 `npm run dev` 启动开发服务器。

## 常用命令
- `npm run build`: 前端生产环境构建。
- `mvn package`: 后端打 Jar 包。
- `docker compose up --build`: 重新构建并启动所有服务。

## 代码规范
- 前端使用 TypeScript + Vue 3 组合式 API。
- 后端遵循 RESTful API 设计规范，异常通过 `RestExceptionHandler` 统一捕获。
