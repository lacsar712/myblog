<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getJson, postJson, deleteJson } from '@/lib/api'
import { toastWarning, toastSuccess } from '@/lib/feedback'
import { useListLoader, confirmAndRun } from '@/composables/useAdmin'
import Button from '@/components/ui/Button.vue'
import Input from '@/components/ui/Input.vue'
import Table from '@/components/ui/Table.vue'
import Card from '@/components/ui/Card.vue'

interface Tag {
  id: string
  name: string
}

const items = ref<Tag[]>([])
const form = ref({ name: '' })

const columns = [
  { key: 'name', title: '名称' },
  { key: 'actions', title: '操作', width: '120px' },
]

const { loading, load: fetchList } = useListLoader(async () => {
  items.value = await getJson<Tag[]>('/admin/tags')
})

async function create() {
  if (!form.value.name) {
    toastWarning('请填写名称')
    return
  }
  await postJson('/admin/tags', form.value)
  form.value = { name: '' }
  toastSuccess('创建成功')
  await fetchList()
}

async function remove(id: string) {
  await confirmAndRun({
    confirm: { title: '确认删除？', content: '删除后不可恢复。', okText: '删除', cancelText: '取消', danger: true },
    action: () => deleteJson(`/admin/tags/${id}`),
    successMsg: '已删除',
    onSuccess: fetchList,
  })
}

onMounted(fetchList)
</script>

<template>
  <div class="space-y-6">
    <h2 class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-600 dark:from-white dark:to-slate-400">标签管理</h2>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <Card title="新建标签" class="md:col-span-1 h-fit">
        <div class="space-y-4">
          <Input v-model="form.name" label="名称" placeholder="标签名称" />
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
