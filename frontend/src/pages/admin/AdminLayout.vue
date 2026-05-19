<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Button from '@/components/ui/Button.vue'

const route = useRoute()
const router = useRouter()

const navItems = [
  { key: 'articles', label: '文章管理', path: '/admin/articles' },
  { key: 'categories', label: '分类管理', path: '/admin/categories' },
  { key: 'tags', label: '标签管理', path: '/admin/tags' },
  { key: 'home', label: '返回前台', path: '/' },
]

const mobileNavOpen = ref(false)

function navigate(path: string) {
  router.push(path)
  mobileNavOpen.value = false
}
</script>

<template>
  <div class="min-h-screen flex bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 font-sans">
    <!-- Sidebar -->
    <aside class="w-64 bg-white dark:bg-slate-900 border-r border-slate-200 dark:border-slate-800 hidden md:flex flex-col">
      <div class="p-6 border-b border-slate-200 dark:border-slate-800">
        <h1 class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">管理后台</h1>
        <p class="text-xs text-slate-500 mt-1">内容管理系统</p>
      </div>
      
      <nav class="flex-1 p-4 space-y-1">
        <template v-for="item in navItems" :key="item.key">
           <button 
             @click="navigate(item.path)"
             class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors"
             :class="route.path.startsWith(item.path) && item.key !== 'home' 
               ? 'bg-blue-50 text-blue-600 dark:bg-blue-950/30 dark:text-blue-400' 
               : 'text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-slate-900 dark:hover:text-slate-200'"
           >
             {{ item.label }}
           </button>
        </template>
      </nav>

      <div class="p-4 border-t border-slate-200 dark:border-slate-800">
        <div class="flex items-center gap-3">
          <div class="h-8 w-8 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center text-xs">U</div>
          <div class="text-sm font-medium">管理员</div>
        </div>
      </div>
    </aside>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col min-w-0">
      <header class="h-16 bg-white/70 dark:bg-slate-900/70 backdrop-blur-md border-b border-slate-200 dark:border-slate-800 flex items-center justify-between px-[15px] md:px-8">
        <div class="flex items-center gap-2 md:hidden">
          <button class="p-2 rounded-lg text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800/50" @click="mobileNavOpen = true">
            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="4" x2="20" y1="12" y2="12"/><line x1="4" x2="20" y1="6" y2="6"/><line x1="4" x2="20" y1="18" y2="18"/></svg>
          </button>
          <div class="font-bold">管理后台</div>
        </div>
        <div class="text-sm font-medium text-slate-500 dark:text-slate-400">
           {{ route.meta.title ?? '仪表盘' }} 
        </div>
      </header>

      <div v-if="mobileNavOpen" class="fixed inset-0 z-50 md:hidden">
        <div class="absolute inset-0 bg-black/40" @click="mobileNavOpen = false"></div>
        <div class="absolute left-1/2 top-24 w-[calc(100%-30px)] -translate-x-1/2">
          <div class="rounded-2xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 shadow-2xl overflow-hidden">
            <div class="px-4 py-3 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between">
              <div class="font-semibold">菜单</div>
              <button class="p-2 rounded-lg text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800/50" @click="mobileNavOpen = false">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
              </button>
            </div>
            <div class="p-3 space-y-2">
              <button
                v-for="item in navItems"
                :key="item.key"
                class="w-full text-left px-4 py-3 rounded-xl text-sm font-medium transition-colors"
                :class="route.path.startsWith(item.path) && item.key !== 'home'
                  ? 'bg-blue-50 text-blue-600 dark:bg-blue-950/30 dark:text-blue-400'
                  : 'text-slate-700 dark:text-slate-200 hover:bg-slate-50 dark:hover:bg-slate-800/50'"
                @click="navigate(item.path)"
              >
                {{ item.label }}
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <main class="flex-1 p-[15px] md:p-8 overflow-y-auto">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
