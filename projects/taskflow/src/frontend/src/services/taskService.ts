import api from './api'
import type { ApiResponse, Task, TaskStatus, TaskPriority } from '../utils/types'

interface TaskFilters {
  page?: number
  project_id?: number
  status_filter?: TaskStatus
  priority?: TaskPriority
  search?: string
}

export async function fetchTasks(filters: TaskFilters = {}): Promise<ApiResponse<Task[]>> {
  const { data } = await api.get('/tasks', { params: filters })
  return data
}

export async function fetchTask(id: number): Promise<ApiResponse<Task>> {
  const { data } = await api.get(`/tasks/${id}`)
  return data
}

export async function createTask(payload: {
  title: string
  description?: string
  status?: TaskStatus
  priority?: TaskPriority
  project_id: number
}): Promise<ApiResponse<Task>> {
  const { data } = await api.post('/tasks', payload)
  return data
}

export async function updateTask(id: number, payload: Partial<Task>): Promise<ApiResponse<Task>> {
  const { data } = await api.patch(`/tasks/${id}`, payload)
  return data
}

export async function deleteTask(id: number): Promise<void> {
  await api.delete(`/tasks/${id}`)
}
