import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import AdminLayout from '@/pages/admin/AdminLayout.vue'
import AdminArticlesPage from '@/pages/admin/AdminArticlesPage.vue'
import ArticleEditPage from '@/pages/admin/ArticleEditPage.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/articles',
    children: [
      {
        path: 'articles',
        name: 'admin-articles',
        component: AdminArticlesPage,
      },
      {
        path: 'categories',
        name: 'admin-categories',
        component: () => import('@/pages/admin/AdminCategoriesPage.vue'),
      },
      {
        path: 'tags',
        name: 'admin-tags',
        component: () => import('@/pages/admin/AdminTagsPage.vue'),
      },
      {
        path: 'articles/new',
        name: 'admin-article-new',
        component: ArticleEditPage,
      },
      {
        path: 'articles/:id/edit',
        name: 'admin-article-edit',
        component: ArticleEditPage,
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
