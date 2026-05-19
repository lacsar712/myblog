<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getJson, type Category } from '@/lib/api'
import { toastWarning } from '@/lib/feedback'
import { useList, useCreate, useDelete } from '@/composables'
import Button from '@/components/ui/Button.vue'
import Input from '@/components/ui/Input.vue'
import Table from '@/components/ui/Table.vue'
import Card from '@/components/ui/Card.vue'

const form = ref({ name: '', slug: '' })

const { loading, items, fetchList } = useList<Category>(
  async () => await getJson('/admin/categories')
)

const { create: submitCreate } = useCreate(
  '/admin/categories',
  form,
  fetchList
)

const { remove } = useDelete(
  (id) => `/admin/categories/${id}`,
  fetchList
)

const columns = [
  { key: 'name', title: '名称' },
  { key: 'slug', title: '别名' },
  { key: 'actions', title: '操作', width: '120px' },
]

async function create() {
  if (!form.value.name || !form.value.slug) {
    toastWarning('请填写完整信息')
    return
  }
  await submitCreate()
}

onMounted(fetchList)
</script>

<template>
  <div class="space-y-6">
    <h2 class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-600 dark:from-white dark:to-slate-400">分类管理</h2>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <Card title="新建分类" class="md:col-span-1 h-fit">
        <div class="space-y-4">
          <Input v-model="form.name" label="名称" placeholder="分类名称" />
          <Input v-model="form.slug" label="别名" placeholder="别名" />
          <Button block @click="create">创建</Button>
        </div>
      </Card>

      <Card class="md:col-span-2" no-padding>
        <Table :items="items" :columns="columns" :loading="loading" borderless>
            <template #actions="{ record }">
                <Button size="sm" variant="danger" @click="remove(record.id)">删除</Button>
            </template>
        </Table>
      </Card>
    </div>
  </div>
</template>
