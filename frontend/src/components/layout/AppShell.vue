<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Button from '@/components/ui/Button.vue'

const router = useRouter()
const isMenuOpen = ref(false)

function goHome() {
  router.push('/')
}

function goAdmin() {
  router.push('/admin/articles')
}
</script>

<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 font-sans transition-colors duration-300">
    <!-- Top Navigation -->
    <header class="sticky top-0 z-50 w-full border-b border-white/10 bg-white/70 dark:bg-slate-950/70 backdrop-blur-xl">
      <div class="h-16 flex items-center justify-between px-[15px]">
        <div class="flex items-center gap-2 cursor-pointer" @click="goHome">
          <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white font-bold shadow-lg shadow-blue-500/20">
            M
          </div>
          <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-600 dark:from-white dark:to-slate-400">
            myblog
          </span>
        </div>

        <div class="hidden md:flex items-center gap-4">
          <Button variant="ghost" size="sm" @click="goHome">首页</Button>
          <Button variant="primary" size="sm" @click="goAdmin">管理后台</Button>
        </div>

        <!-- Mobile Menu Button -->
        <button class="md:hidden p-2 text-slate-600 dark:text-slate-400" @click="isMenuOpen = !isMenuOpen">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="4" x2="20" y1="12" y2="12"/><line x1="4" x2="20" y1="6" y2="6"/><line x1="4" x2="20" y1="18" y2="18"/></svg>
        </button>
      </div>

      <!-- Mobile Menu -->
      <div v-if="isMenuOpen" class="md:hidden border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 p-4 space-y-2">
        <Button variant="ghost" block @click="goHome">首页</Button>
        <Button variant="primary" block @click="goAdmin">管理后台</Button>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-[15px] py-8">
      <slot />
    </main>

    <!-- Footer -->
    <footer class="border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-950 py-8 mt-auto">
      <div class="px-[15px] text-center text-slate-500 text-sm">
        &copy; {{ new Date().getFullYear() }} myblog ｜ 版权所有
      </div>
    </footer>
  </div>
</template>
