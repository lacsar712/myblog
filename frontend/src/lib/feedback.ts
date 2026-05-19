import { Modal, message } from 'ant-design-vue'

export function toastSuccess(content: string) {
  message.success({ content })
}

export function toastError(content: string) {
  message.error({ content })
}

export function toastInfo(content: string) {
  message.info({ content })
}

export function toastWarning(content: string) {
  message.warning({ content })
}

export function confirmModal(opts: {
  title: string
  content?: string
  okText?: string
  cancelText?: string
  danger?: boolean
}) {
  return new Promise<boolean>((resolve) => {
    Modal.confirm({
      title: opts.title,
      content: opts.content,
      centered: true,
      okText: opts.okText ?? '确定',
      cancelText: opts.cancelText ?? '取消',
      okType: opts.danger ? 'danger' : 'primary',
      onOk: () => resolve(true),
      onCancel: () => resolve(false),
    })
  })
}

