<script setup lang="ts">
import { onMounted, ref } from 'vue'
import dayjs from 'dayjs'
import { getJson, type Article, type PageResult } from '@/lib/api'
import { renderMarkdown } from '@/lib/markdown'
import { toastError } from '@/lib/feedback'
import AppShell from '@/components/layout/AppShell.vue'
import Card from '@/components/ui/Card.vue'
import Button from '@/components/ui/Button.vue'

const loading = ref(false)
const items = ref<Article[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0) // Although we might not show full pagination for simple redesign

// Reading state
const reading = ref(false)
const current = ref<Article | null>(null)
const currentHtml = ref('')

async function fetchList() {
  loading.value = true
  try {
    const data = await getJson<PageResult<Article>>('/articles', {
      page: page.value,
      pageSize: pageSize.value,
      status: 'published'
    })
    items.value = data.items
    total.value = data.total
  } finally {
    loading.value = false
  }
}

async function openArticle(article: Article) {
  reading.value = true
  current.value = article
  // Populate content immediately if available, or fetch full detail if needed
  // Optimization: if list already has summary, maybe fetch full content by ID
  try {
    const detail = await getJson<Article>(`/articles/${article.id}`)
    current.value = detail
    currentHtml.value = renderMarkdown(detail.contentMd)
  } catch (e) {
    toastError('加载文章失败，请稍后重试')
    closeReader()
  }
}

function closeReader() {
  reading.value = false
  current.value = null
  currentHtml.value = ''
}

onMounted(fetchList)
</script>

<template>
  <AppShell>
    <div class="max-w-7xl mx-auto grid grid-cols-1 gap-8 relative">
      
      <!-- Article List -->
      <div v-if="!reading" class="space-y-6 animate-in slide-in-from-bottom-4 duration-500">
        <div class="flex items-center justify-between pointer-events-none">
           <h1 class="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-600 dark:from-white dark:to-slate-400">
             最新发布
           </h1>
        </div>

        <div v-if="loading && items.length === 0" class="flex justify-center py-20">
           <div class="animate-spin h-8 w-8 border-4 border-primary rounded-full border-t-transparent"></div>
        </div>

        <div v-else-if="items.length === 0" class="text-center py-20 text-slate-500">
          暂无文章。
        </div>

        <div v-else class="grid gap-6">
          <Card v-for="item in items" :key="item.id" class="group transition-all hover:shadow-2xl hover:-translate-y-1 cursor-pointer" @click="openArticle(item)">
            <article>
              <div class="flex items-center gap-2 mb-2">
                 <span v-if="item.category" class="px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-300">
                    {{ item.category.name }}
                 </span>
                 <span v-else class="px-2 py-0.5 rounded-full text-xs font-medium bg-slate-100 text-slate-600 dark:bg-slate-800 dark:text-slate-400">
                    未分类
                 </span>
              </div>
              <h2 class="text-2xl font-bold text-slate-900 dark:text-white mb-2 group-hover:text-primary transition-colors">
                {{ item.title }}
              </h2>
              <div class="flex items-center gap-3 text-sm text-slate-500 dark:text-slate-400 mb-4">
                <span>{{ dayjs(item.publishedAt).format('YYYY年M月D日') }}</span>
                <div v-if="item.tags && item.tags.length > 0" class="flex gap-1">
                   <span>·</span>
                   <span v-for="t in item.tags" :key="t.id" class="text-slate-400">#{{ t.name }}</span>
                </div>
              </div>
              <p class="text-slate-600 dark:text-slate-300 leading-relaxed line-clamp-3">
                {{ item.summary }}
              </p>
              <div class="mt-4 flex items-center text-primary font-medium opacity-0 group-hover:opacity-100 transition-opacity -translate-x-2 group-hover:translate-x-0 duration-300">
                阅读全文 ->
              </div>
            </article>
          </Card>
        </div>
      </div>

      <!-- Article Reader (Overlay/Drawer style within page) -->
      <div v-else class="animate-in fade-in zoom-in-95 duration-300">
         <Button variant="ghost" class="mb-6 -ml-2" @click="closeReader">
           ← 返回列表
         </Button>

         <Card class="min-h-[80vh]">
           <header class="mb-8 border-b border-slate-100 dark:border-slate-800 pb-8">
             <div class="flex items-center gap-3 mb-4">
               <span v-if="current?.category" class="px-2.5 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-300">
                  {{ current.category.name }}
               </span>
               <span v-if="current?.tags" v-for="t in current.tags" :key="t.id" class="text-sm text-slate-500">
                  #{{ t.name }}
               </span>
             </div>
             <h1 class="text-3xl md:text-5xl font-extrabold text-slate-900 dark:text-white mb-4 tracking-tight">
               {{ current?.title }}
             </h1>
             <div class="text-slate-500 dark:text-slate-400">
               发布于 {{ dayjs(current?.publishedAt).format('YYYY年M月D日 HH:mm') }}
             </div>
           </header>
           
           <div class="markdown-body prose dark:prose-invert max-w-none" v-html="currentHtml"></div>
         </Card>
      </div>

    </div>
  </AppShell>
</template>

<style scoped>
/* Scoped overrides if needed */
</style>
