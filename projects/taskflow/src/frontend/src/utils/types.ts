export type TaskStatus = 'todo' | 'in_progress' | 'done'
export type TaskPriority = 'low' | 'medium' | 'high'

export interface Project {
  id: number
  name: string
  description: string | null
  task_count: number
  created_at: string
  updated_at: string
}

export interface Task {
  id: number
  title: string
  description: string | null
  status: TaskStatus
  priority: TaskPriority
  project_id: number
  created_at: string
  updated_at: string
}

export interface ApiResponse<T> {
  data: T
  error: string | null
  meta: {
    page?: number
    per_page?: number
    total?: number
  }
}
