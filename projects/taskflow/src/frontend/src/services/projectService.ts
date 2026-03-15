import api from './api'
import type { ApiResponse, Project } from '../utils/types'

export async function fetchProjects(page = 1): Promise<ApiResponse<Project[]>> {
  const { data } = await api.get('/projects', { params: { page } })
  return data
}

export async function fetchProject(id: number): Promise<ApiResponse<Project>> {
  const { data } = await api.get(`/projects/${id}`)
  return data
}

export async function createProject(payload: { name: string; description?: string }): Promise<ApiResponse<Project>> {
  const { data } = await api.post('/projects', payload)
  return data
}

export async function updateProject(id: number, payload: { name?: string; description?: string }): Promise<ApiResponse<Project>> {
  const { data } = await api.patch(`/projects/${id}`, payload)
  return data
}

export async function deleteProject(id: number): Promise<void> {
  await api.delete(`/projects/${id}`)
}
