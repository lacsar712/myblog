<script setup lang="ts" generic="T">
const props = withDefaults(
  defineProps<{
    items: T[]
    loading?: boolean
    borderless?: boolean
    columns: {
      key: string
      title: string
      width?: string
    }[]
  }>(),
  {
    loading: false,
    borderless: false,
  },
)
</script>

<template>
  <div :class="props.borderless ? 'overflow-hidden' : 'overflow-hidden rounded-lg border border-slate-200 dark:border-slate-800'">
    <div class="overflow-x-auto">
      <table class="w-full text-left text-sm text-slate-600 dark:text-slate-400">
        <thead class="bg-slate-50 dark:bg-slate-900 text-slate-900 dark:text-slate-200 font-semibold border-b border-slate-200 dark:border-slate-800">
          <tr>
            <th v-for="col in columns" :key="col.key" class="p-4" :style="{ width: col.width }">
              {{ col.title }}
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-200 dark:divide-slate-800 bg-white dark:bg-slate-950">
          <tr v-if="loading">
            <td :colspan="columns.length" class="p-8 text-center">
              <div class="inline-block animate-spin h-6 w-6 border-2 border-primary rounded-full border-t-transparent"></div>
            </td>
          </tr>
          <tr v-else-if="items.length === 0">
             <td :colspan="columns.length" class="p-8 text-center text-slate-500">
              No data available
            </td>
          </tr>
          <tr v-else v-for="(item, idx) in items" :key="idx" class="hover:bg-slate-50 dark:hover:bg-slate-900/50 transition-colors">
            <td v-for="col in columns" :key="col.key" class="p-4 align-middle">
              <slot :name="col.key" :record="item" :item="item">
                {{ (item as any)[col.key] }}
              </slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
