<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getJson, postJson, type Article, type PageResult } from '@/lib/api'
import dayjs from 'dayjs'
import { useListLoader, confirmAndRun } from '@/composables/useAdmin'
import Button from '@/components/ui/Button.vue'
import Input from '@/components/ui/Input.vue'
import Table from '@/components/ui/Table.vue'
import Card from '@/components/ui/Card.vue'

const router = useRouter()

const keyword = ref('')
const items = ref<Article[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const columns = [
  { key: 'title', title: '标题' },
  { key: 'status', title: '状态', width: '120px' },
  { key: 'updatedAt', title: '更新时间', width: '180px' },
  { key: 'actions', title: '操作', width: '220px' },
]

const { loading, load: fetchList } = useListLoader(async () => {
  const data = await getJson<PageResult<Article>>('/admin/articles', {
    keyword: keyword.value || undefined,
    page: page.value,
    pageSize: pageSize.value
  })
  items.value = data.items
  total.value = data.total
})

async function togglePublish(a: Article) {
  await confirmAndRun({
    confirm: {
      title: a.status === 'published' ? '确认取消发布？' : '确认发布？',
      content: a.status === 'published' ? '取消发布后，文章将从首页隐藏。' : '发布后，文章将出现在首页。',
      okText: '确定',
      cancelText: '取消',
    },
    action: () => postJson(`/admin/articles/${a.id}/${a.status === 'published' ? 'unpublish' : 'publish'}`),
    successMsg: a.status === 'published' ? '已取消发布' : '已发布',
    onSuccess: fetchList,
  })
}

function handleEdit(a: Article) {
  router.push(`/admin/articles/${a.id}/edit`)
}

function handleCreate() {
  router.push('/admin/articles/new')
}

onMounted(fetchList)
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <h2 class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-600 dark:from-white dark:to-slate-400">文章管理</h2>
      <Button @click="handleCreate">
        + 新建文章
      </Button>
    </div>

    <Card no-padding>
      <div class="p-4 border-b border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/50 flex flex-col sm:flex-row gap-3">
        <Input 
          v-model="keyword" 
          placeholder="搜索文章..." 
          class="w-full sm:max-w-xs"
          @keydown.enter="fetchList"
        />
        <Button variant="secondary" @click="fetchList">查询</Button>
      </div>

      <Table :items="items" :columns="columns" :loading="loading" borderless>
        <template #title="{ record }">
          <div class="font-medium text-slate-900 dark:text-slate-100">{{ record.title }}</div>
          <div class="text-xs text-slate-500 truncate max-w-xs">{{ record.summary }}</div>
        </template>
        
        <template #status="{ record }">
          <span 
            class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium"
            :class="record.status === 'published' 
              ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400' 
              : 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400'"
          >
            <span class="w-1.5 h-1.5 rounded-full mr-1.5" :class="record.status === 'published' ? 'bg-green-500' : 'bg-yellow-500'"></span>
            {{ record.status === 'published' ? '已发布' : '草稿' }}
          </span>
        </template>

        <template #updatedAt="{ record }">
          {{ dayjs(record.updatedAt).format('YYYY-MM-DD HH:mm') }}
        </template>

        <template #actions="{ record }">
          <div class="flex items-center gap-2">
            <Button size="sm" variant="secondary" @click="handleEdit(record)">编辑</Button>
            <Button 
              size="sm" 
              :variant="record.status === 'published' ? 'secondary' : 'primary'"
              @click="togglePublish(record)"
            >
              {{ record.status === 'published' ? '取消发布' : '发布' }}
            </Button>
          </div>
        </template>
      </Table>

      <div class="p-4 border-t border-slate-200 dark:border-slate-800 flex justify-between items-center text-sm text-slate-500">
        <div>共 {{ total }} 篇文章</div>
        <!-- Simple Pagination could be added here -->
      </div>
    </Card>
  </div>
</template>
