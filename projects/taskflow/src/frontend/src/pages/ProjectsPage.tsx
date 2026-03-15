import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Plus, Trash2, FolderKanban } from 'lucide-react'
import { useProjects, useDeleteProject } from '../hooks/useProjects'
import CreateProjectModal from '../components/CreateProjectModal'

export default function ProjectsPage() {
  const [showModal, setShowModal] = useState(false)
  const { data: res, isLoading } = useProjects()
  const deleteProject = useDeleteProject()

  const projects = res?.data ?? []

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="font-display text-2xl font-bold">Projects</h1>
        <button onClick={() => setShowModal(true)} className="btn-primary">
          <Plus size={16} />
          New Project
        </button>
      </div>

      {isLoading ? (
        <p className="text-sm text-slate-400">Loading...</p>
      ) : projects.length === 0 ? (
        <div className="card flex flex-col items-center justify-center py-16 text-center">
          <FolderKanban size={40} className="text-slate-300 mb-3" />
          <p className="text-sm text-slate-400 mb-4">No projects yet</p>
          <button onClick={() => setShowModal(true)} className="btn-primary">
            <Plus size={16} />
            Create your first project
          </button>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {projects.map((p) => (
            <div key={p.id} className="card group relative hover:shadow-md transition-shadow">
              <Link to={`/projects/${p.id}`} className="block">
                <h3 className="text-sm font-bold mb-1">{p.name}</h3>
                {p.description && (
                  <p className="text-xs text-slate-400 line-clamp-2 mb-3">{p.description}</p>
                )}
                <span className="badge bg-slate-100 text-slate-500">{p.task_count} tasks</span>
              </Link>
              <button
                onClick={(e) => {
                  e.preventDefault()
                  if (confirm(`Delete "${p.name}" and all its tasks?`)) {
                    deleteProject.mutate(p.id)
                  }
                }}
                className="absolute top-4 right-4 text-slate-300 opacity-0 group-hover:opacity-100 hover:text-danger transition-all"
                title="Delete project"
              >
                <Trash2 size={15} />
              </button>
            </div>
          ))}
        </div>
      )}

      <CreateProjectModal open={showModal} onClose={() => setShowModal(false)} />
    </div>
  )
}
