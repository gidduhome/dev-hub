import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { fetchTasks, createTask, updateTask, deleteTask } from '../services/taskService'
import type { TaskStatus, TaskPriority } from '../utils/types'

const KEYS = {
  all: ['tasks'] as const,
  filtered: (filters: Record<string, unknown>) => ['tasks', filters] as const,
}

export function useTasks(filters: {
  page?: number
  project_id?: number
  status_filter?: TaskStatus
  priority?: TaskPriority
  search?: string
} = {}) {
  return useQuery({
    queryKey: KEYS.filtered(filters),
    queryFn: () => fetchTasks(filters),
  })
}

export function useCreateTask() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: createTask,
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: KEYS.all })
      qc.invalidateQueries({ queryKey: ['projects'] })
    },
  })
}

export function useUpdateTask() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, ...payload }: { id: number } & Partial<Record<string, unknown>>) =>
      updateTask(id, payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  })
}

export function useDeleteTask() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: deleteTask,
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: KEYS.all })
      qc.invalidateQueries({ queryKey: ['projects'] })
    },
  })
}
