import { ref, type Ref } from 'vue'
import { deleteJson, postJson, putJson } from '@/lib/api'
import { confirmModal, toastSuccess } from '@/lib/feedback'

export function useDelete(
  getUrl: (id: string) => string,
  onSuccess?: () => Promise<void> | void,
) {
  const deleting = ref<string | null>(null)

  async function remove(id: string) {
    const ok = await confirmModal({
      title: '确认删除？',
      content: '删除后不可恢复。',
      okText: '删除',
      cancelText: '取消',
      danger: true,
    })
    if (!ok) return

    deleting.value = id
    try {
      await deleteJson(getUrl(id))
      toastSuccess('已删除')
      await onSuccess?.()
    } finally {
      deleting.value = null
    }
  }

  return {
    deleting,
    remove,
  }
}

export function useCreate<F extends Record<string, unknown>>(
  url: string,
  form: Ref<F>,
  onSuccess?: () => Promise<void> | void,
) {
  const submitting = ref(false)

  async function create() {
    submitting.value = true
    try {
      await postJson(url, form.value)
      Object.keys(form.value).forEach((key) => {
        ;(form.value as Record<string, unknown>)[key] = ''
      })
      toastSuccess('创建成功')
      await onSuccess?.()
    } finally {
      submitting.value = false
    }
  }

  return {
    submitting,
    create,
  }
}

export function useUpdate<F extends Record<string, unknown>>(
  getUrl: (id: string) => string,
  onSuccess?: () => Promise<void> | void,
) {
  const updating = ref<string | null>(null)

  async function update(id: string, body: F) {
    updating.value = id
    try {
      await putJson(getUrl(id), body)
      toastSuccess('更新成功')
      await onSuccess?.()
    } finally {
      updating.value = null
    }
  }

  return {
    updating,
    update,
  }
}

export function useConfirmAction() {
  const loading = ref(false)

  async function execute(
    action: () => Promise<void>,
    options: {
      title: string
      content?: string
      okText?: string
      cancelText?: string
      danger?: boolean
      successMessage?: string
    },
  ) {
    const ok = await confirmModal({
      title: options.title,
      content: options.content,
      okText: options.okText,
      cancelText: options.cancelText,
      danger: options.danger,
    })
    if (!ok) return false

    loading.value = true
    try {
      await action()
      if (options.successMessage) {
        toastSuccess(options.successMessage)
      }
      return true
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    execute,
  }
}
