# MY BLOG - 现代个人博客系统

## 🛠 技术栈
- **前端**: Vue 3 + Vite + TypeScript + Ant Design Vue + Tailwind CSS
- **后端**: Spring Boot 3 (Java 17) + Spring Data JPA
- **数据库**: MySQL 8.0
- **代理**: Nginx

## 🌟 核心特性
- **极致响应式**: 完美适配 Mobile/Tablet/PC，移动端拥有独立的弹层导航菜单。
- **Markdown 支持**: 支持全功能 Markdown 写作与阅读，优化了代码块、图片与表格的自适应显示。
- **现代化 UI**: 采用 Ant Design Vue 组件库，所有弹窗居中显示，Toast 自动消失。
- **即刻部署**: 完整的 Docker Compose 编排，一键启动全栈服务。

## 🚀 快速启动
```bash
cd myblog
docker compose up --build
```
- 前台首页: [http://localhost:3501](http://localhost:3501)
- 管理后台: [http://localhost:3501/admin/articles](http://localhost:3501/admin/articles)

## 📁 目录结构
- `backend/`: Spring Boot 后端源码。
- `frontend/`: Vue 3 前端源码。
- `mysql/`: 数据库初始化脚本及持久化配置。
- `docs/`: 包含架构、设计、开发及测试等详细文档。

## 📄 相关文档
- [系统架构](docs/ARCHITECTURE.md)
- [详细设计](docs/DESIGN.md)
- [开发指南](docs/DEVELOPMENT.md)
- [测试报告](docs/TESTING.md)
- [用户手册](docs/USER_MANUAL.md)
