import { onMounted, ref, type Ref } from 'vue'
import { confirmModal, toastSuccess } from '@/lib/feedback'

export interface UseListOptions<TItem, TQuery> {
  load: (query?: TQuery) => Promise<TItem[] | { items: TItem[]; total: number }>
  remove?: (id: string) => Promise<void>
  confirm?: {
    title: string
    content?: string
    okText?: string
    cancelText?: string
  }
  successMessage?: string
  loadOnMount?: boolean
}

export interface UseListReturn<TItem, TQuery> {
  items: Ref<TItem[]>
  loading: Ref<boolean>
  total: Ref<number>
  fetchList: (query?: TQuery) => Promise<void>
  handleRemove: (id: string) => Promise<void>
}

function isPagedResult<TItem>(result: TItem[] | { items: TItem[]; total: number }): result is { items: TItem[]; total: number } {
  return result !== null && typeof result === 'object' && Array.isArray((result as { items?: unknown }).items)
}

export function useList<TItem, TQuery = undefined>(
  options: UseListOptions<TItem, TQuery>,
): UseListReturn<TItem, TQuery> {
  const items = ref<TItem[]>([]) as Ref<TItem[]>
  const loading = ref(false)
  const total = ref(0)

  async function fetchList(query?: TQuery) {
    loading.value = true
    try {
      const result = await options.load(query)
      if (isPagedResult<TItem>(result)) {
        items.value = result.items
        total.value = result.total
      } else {
        items.value = result
        total.value = result.length
      }
    } finally {
      loading.value = false
    }
  }

  async function handleRemove(id: string) {
    if (!options.confirm) return
    const ok = await confirmModal({ ...options.confirm, danger: true })
    if (!ok) return
    if (options.remove) {
      await options.remove(id)
    }
    if (options.successMessage) {
      toastSuccess(options.successMessage)
    }
    await fetchList()
  }

  if (options.loadOnMount !== false) {
    onMounted(() => fetchList())
  }

  return { items, loading, total, fetchList, handleRemove }
}
