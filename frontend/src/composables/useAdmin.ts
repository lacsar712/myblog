import { ref } from 'vue'
import { confirmModal, toastSuccess } from '@/lib/feedback'

export function useListLoader<T>(fetcher: () => Promise<T>) {
  const loading = ref(false)

  async function load() {
    loading.value = true
    try {
      return await fetcher()
    } finally {
      loading.value = false
    }
  }

  return { loading, load }
}

export async function confirmAndRun(opts: {
  confirm: { title: string; content?: string; okText?: string; cancelText?: string; danger?: boolean }
  action: () => Promise<void>
  successMsg: string
  onSuccess?: () => Promise<void> | void
}) {
  const ok = await confirmModal(opts.confirm)
  if (!ok) return
  await opts.action()
  toastSuccess(opts.successMsg)
  await opts.onSuccess?.()
}
