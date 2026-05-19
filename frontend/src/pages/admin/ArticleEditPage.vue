<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { onBeforeRouteLeave, useRoute, useRouter } from 'vue-router'
import { getJson, postJson, putJson, type Article } from '@/lib/api'
import { renderMarkdown } from '@/lib/markdown'
import { confirmModal, toastSuccess } from '@/lib/feedback'
import Button from '@/components/ui/Button.vue'
import Input from '@/components/ui/Input.vue'
import Card from '@/components/ui/Card.vue'

const route = useRoute()
const router = useRouter()

const id = computed(() => (typeof route.params.id === 'string' ? route.params.id : null))
const isNew = computed(() => !id.value)

const loading = ref(false)
const saving = ref(false)
const dirty = ref(false)

const form = ref({
  title: '',
  slug: '',
  summary: '',
  contentMd: '',
  categoryId: '' as string | null,
  tagIds: [] as string[],
})

const categories = ref<{id: string, name: string, slug: string}[]>([])
const tags = ref<{id: string, name: string}[]>([])

const previewHtml = computed(() => renderMarkdown(form.value.contentMd))

function deriveSlug(title: string) {
  return title
    .trim()
    .toLowerCase()
    .replace(/[^a-z0-9\u4e00-\u9fa5\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .replace(/^-|-$/g, '')
}

async function load() {
  loading.value = true
  try {
    const [catData, tagData] = await Promise.all([
      getJson<{id: string, name: string, slug: string}[]>('/admin/categories'),
      getJson<{id: string, name: string}[]>('/admin/tags')
    ])
    categories.value = catData
    tags.value = tagData

    if (id.value) {
      const a = await getJson<Article>(`/admin/articles/${id.value}`)
      form.value = {
        title: a.title,
        slug: a.slug,
        summary: a.summary,
        contentMd: a.contentMd,
        categoryId: a.category ? a.category.id : '',
        tagIds: a.tags ? a.tags.map(t => t.id) : [],
      }
      dirty.value = false
    }
  } finally {
    loading.value = false
  }
}

async function save() {
  saving.value = true
  try {
    // Convert empty string categoryId to null for backend
    const payload = {
        ...form.value,
        categoryId: form.value.categoryId === '' ? null : form.value.categoryId
    }

    if (isNew.value) {
      const created = await postJson<Article>('/admin/articles', payload)
      toastSuccess('已保存草稿')
      dirty.value = false
      router.replace(`/admin/articles/${created.id}/edit`)
      return
    }
    await putJson<Article>(`/admin/articles/${id.value}`, payload)
    toastSuccess('保存成功')
    dirty.value = false
  } finally {
    saving.value = false
  }
}

async function publish() {
  if (isNew.value) {
    await save()
  }
  if (!id.value) return
  await postJson<Article>(`/admin/articles/${id.value}/publish`)
  toastSuccess('发布成功')
}

async function unpublish() {
  if (!id.value) return
  await postJson<Article>(`/admin/articles/${id.value}/unpublish`)
  toastSuccess('取消发布成功')
}

function backToList() {
  router.push('/admin/articles')
}

onMounted(load)

onBeforeRouteLeave(async () => {
  if (!dirty.value) return true
  return confirmModal({ title: '确认离开？', content: '您有未保存的更改，确认离开吗？', okText: '离开', cancelText: '继续编辑' })
})
</script>

<template>
  <div class="flex flex-col">
    <!-- Toolbar -->
    <Card no-padding class="mb-4 flex-none">
      <div class="px-4 py-3 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <Button variant="ghost" size="sm" @click="backToList">
            ← 返回
          </Button>
          <div class="h-6 w-px bg-slate-200 dark:bg-slate-700 mx-1"></div>
          <span class="font-semibold text-slate-900 dark:text-white">
            {{ isNew ? '新建文章' : '编辑文章' }}
          </span>
          <span v-if="dirty" class="text-xs text-yellow-600 bg-yellow-100 dark:bg-yellow-900/30 dark:text-yellow-400 px-2 py-0.5 rounded-full">
            未保存
          </span>
        </div>
        <div class="flex items-center gap-2">
          <Button variant="secondary" :loading="saving" @click="save">
            保存草稿
          </Button>
          <Button variant="primary" @click="publish">
            发布
          </Button>
           <Button v-if="!isNew" variant="danger" size="sm" @click="unpublish">
            取消发布
          </Button>
        </div>
      </div>
    </Card>

    <!-- Editor Area -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
      <!-- Edit Form -->
      <Card no-padding class="flex flex-col min-h-0 overflow-hidden">
        <div class="flex-1 min-h-0 overflow-y-auto p-4 flex flex-col gap-4">
          <Input 
            v-model="form.title" 
            label="标题" 
            placeholder="文章标题"
            @input="() => {
              dirty = true
              if (!form.slug) form.slug = deriveSlug(form.title)
            }" 
          />
          
          <Input 
            v-model="form.slug" 
            label="别名 (Slug)" 
            placeholder="文章URL别名"
            @input="dirty = true" 
          />
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
             <div class="space-y-1">
               <label class="block text-sm font-medium text-slate-700 dark:text-slate-200">分类</label>
               <select 
                 v-model="form.categoryId"
                 class="block w-full rounded-lg border-slate-200 bg-white/50 dark:bg-slate-800/50 dark:border-slate-700 px-4 py-2.5 text-slate-900 dark:text-white focus:border-primary focus:ring-2 focus:ring-primary/20 sm:text-sm transition-shadow appearance-none"
                 @change="dirty = true"
               >
                 <option value="">(无分类)</option>
                 <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
               </select>
             </div>

             <div class="space-y-1">
               <label class="block text-sm font-medium text-slate-700 dark:text-slate-200">标签</label>
               <div class="flex flex-wrap gap-2 min-h-[42px] content-center">
                 <label 
                   v-for="t in tags" 
                   :key="t.id" 
                   class="inline-flex items-center px-3 py-1 rounded-full border text-xs cursor-pointer transition-colors select-none"
                   :class="form.tagIds.includes(t.id) 
                     ? 'bg-blue-50 border-blue-200 text-blue-700 dark:bg-blue-900/30 dark:border-blue-800 dark:text-blue-400' 
                     : 'bg-white border-slate-200 text-slate-600 dark:bg-slate-800 dark:border-slate-700 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700'"
                 >
                   <input 
                     type="checkbox" 
                     :value="t.id" 
                     v-model="form.tagIds" 
                     class="hidden"
                     @change="dirty = true"
                   >
                   {{ t.name }}
                 </label>
                 <span v-if="tags.length === 0" class="text-xs text-slate-400 self-center">暂无标签，请前往标签管理添加</span>
               </div>
             </div>
          </div>
          
          <div class="space-y-1">
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-200">摘要</label>
            <textarea
              v-model="form.summary"
              rows="3"
              class="block w-full rounded-lg border-slate-200 bg-white/50 dark:bg-slate-800/50 dark:border-slate-700 px-4 py-2.5 text-slate-900 dark:text-white focus:border-primary focus:ring-2 focus:ring-primary/20 sm:text-sm transition-shadow"
              placeholder="简短摘要..."
              @input="dirty = true"
            ></textarea>
            <div class="text-right text-xs text-slate-400">{{ form.summary.length }}/500</div>
          </div>

          <div class="space-y-1 flex-1 flex flex-col min-h-[300px]">
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-200">正文 (Markdown)</label>
            <textarea
              v-model="form.contentMd"
              class="flex-1 block w-full rounded-lg border-slate-200 bg-white/50 dark:bg-slate-800/50 dark:border-slate-700 px-4 py-2.5 text-slate-900 dark:text-white font-mono text-sm focus:border-primary focus:ring-2 focus:ring-primary/20 transition-shadow resize-none"
              placeholder="# 开始写作..."
              @input="dirty = true"
            ></textarea>
          </div>
        </div>
      </Card>

      <!-- Preview -->
      <Card no-padding class="flex flex-col min-h-0 overflow-hidden hidden lg:flex bg-slate-50 dark:bg-slate-900/50">
        <div class="px-4 py-2 border-b border-slate-200 dark:border-slate-800 text-xs font-semibold text-slate-500 uppercase tracking-wider">
          实时预览
        </div>
        <div class="flex-1 overflow-y-auto p-6">
           <div class="markdown-body prose dark:prose-invert max-w-none" v-html="previewHtml"></div>
        </div>
      </Card>
    </div>
  </div>
</template>
