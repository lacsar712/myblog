# 项目设计文档

## 核心功能设计

### 1. 文章管理
- **状态流转**: 默认为“草稿 (draft)”，手动发布后变为“已发布 (published)”。
- **DTO 转换**: 后端通过 `toDto` 方法将实体类转换为包含分类名和标签列表的 DTO，并使用 `@Transactional` 确保懒加载数据在 Session 内加载。

### 2. 响应式布局
- **自适应策略**: 采用 Tailwind CSS 的断点系统（`sm`, `md`, `lg`）。
- **侧边栏设计**: 后台管理系统在移动端（`<768px`）隐藏固定侧边栏，改为顶部“菜单”按钮触发的居中弹层导航。

### 3. UI/UX 交互
- **反馈机制**: 弃用原生 `alert/confirm`，采用 Ant Design Vue 的居中 `Modal` 和自动消失的 `message` toast。
- **视觉风格**: 现代扁平化设计，支持深色模式适配。

## 数据库设计
- `articles`: 存储文章主体。
- `categories`: 一对多关联文章。
- `tags`: 多对多关联文章（通过 `article_tags` 中间表）。
