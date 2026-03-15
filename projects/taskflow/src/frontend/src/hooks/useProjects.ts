import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { fetchProjects, fetchProject, createProject, updateProject, deleteProject } from '../services/projectService'

const KEYS = {
  all: ['projects'] as const,
  detail: (id: number) => ['projects', id] as const,
}

export function useProjects(page = 1) {
  return useQuery({
    queryKey: [...KEYS.all, page],
    queryFn: () => fetchProjects(page),
  })
}

export function useProject(id: number) {
  return useQuery({
    queryKey: KEYS.detail(id),
    queryFn: () => fetchProject(id),
    enabled: id > 0,
  })
}

export function useCreateProject() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: createProject,
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  })
}

export function useUpdateProject() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, ...payload }: { id: number; name?: string; description?: string }) =>
      updateProject(id, payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  })
}

export function useDeleteProject() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: deleteProject,
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  })
}
