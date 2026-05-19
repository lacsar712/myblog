import axios from 'axios'
import { notification } from 'ant-design-vue'

export type ApiError = {
  code: string
  message: string
}

export type ApiResponse<T> = {
  success: boolean
  data: T
  error: ApiError | null
}

export type PageResult<T> = {
  items: T[]
  page: number
  pageSize: number
  total: number
}

export type ArticleStatus = 'draft' | 'published'

export type Category = {
  id: string
  name: string
  slug: string
}

export type Tag = {
  id: string
  name: string
}

export type Article = {
  id: string
  title: string
  slug: string
  summary: string
  contentMd: string
  status: ArticleStatus
  category: Category | null
  tags: Tag[]
  publishedAt: string | null
  createdAt: string
  updatedAt: string
}

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE ?? '/api',
  timeout: 15000,
})

api.interceptors.response.use(
  (resp) => resp,
  (err) => {
    const msg = err?.response?.data?.error?.message ?? err?.message ?? '网络错误'
    notification.error({ message: '请求失败', description: msg })
    return Promise.reject(err)
  },
)

function handleResponse<T>(resp: { data: ApiResponse<T> }): T {
  if (!resp.data.success) {
    notification.error({
      message: resp.data.error?.code ?? '请求失败',
      description: resp.data.error?.message ?? '未知错误',
    })
    throw new Error(resp.data.error?.message ?? 'Request failed')
  }
  return resp.data.data
}

export async function getJson<T>(url: string, params?: Record<string, unknown>) {
  const resp = await api.get<ApiResponse<T>>(url, { params })
  return handleResponse(resp)
}

export async function postJson<T>(url: string, body?: unknown) {
  const resp = await api.post<ApiResponse<T>>(url, body)
  return handleResponse(resp)
}

export async function putJson<T>(url: string, body?: unknown) {
  const resp = await api.put<ApiResponse<T>>(url, body)
  return handleResponse(resp)
}

export async function deleteJson<T>(url: string) {
  const resp = await api.delete<ApiResponse<T>>(url)
  return handleResponse(resp)
}
