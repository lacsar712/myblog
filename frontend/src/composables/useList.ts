import { ref, type Ref } from 'vue'
import { getJson, type PageResult } from '@/lib/api'

type ListFetcher<T> = () => Promise<T[]>
type PageFetcher<T> = () => Promise<PageResult<T>>

export function useList<T>(
  fetcher: ListFetcher<T>,
) {
  const loading = ref(false)
  const items = ref<T[]>([]) as Ref<T[]>

  async function fetchList() {
    loading.value = true
    try {
      items.value = await fetcher()
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    items,
    fetchList,
  }
}

export function usePaginatedList<T>(
  fetcher: PageFetcher<T>,
  initialPage = 1,
  initialPageSize = 10,
) {
  const loading = ref(false)
  const items = ref<T[]>([]) as Ref<T[]>
  const page = ref(initialPage)
  const pageSize = ref(initialPageSize)
  const total = ref(0)

  async function fetchList() {
    loading.value = true
    try {
      const data = await fetcher()
      items.value = data.items
      total.value = data.total
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    items,
    page,
    pageSize,
    total,
    fetchList,
  }
}
