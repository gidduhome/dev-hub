import { useState } from 'react'
import { X } from 'lucide-react'
import { useCreateProject } from '../hooks/useProjects'

interface CreateProjectModalProps {
  open: boolean
  onClose: () => void
}

export default function CreateProjectModal({ open, onClose }: CreateProjectModalProps) {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [error, setError] = useState<string | null>(null)
  const createProject = useCreateProject()

  if (!open) return null

  const handleSubmit = (e: { preventDefault: () => void }) => {
    e.preventDefault()
    if (!name.trim()) return
    setError(null)
    createProject.mutate(
      { name: name.trim(), description: description.trim() || undefined },
      {
        onSuccess: () => {
          setName('')
          setDescription('')
          setError(null)
          onClose()
        },
        onError: (err: Error) => {
          setError(err.message || 'Failed to create project. Please try again.')
        },
      },
    )
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-ink/40 backdrop-blur-sm">
      <div className="card w-full max-w-md mx-4">
        <div className="flex items-center justify-between mb-5">
          <h2 className="font-display text-lg font-bold">New Project</h2>
          <button onClick={onClose} className="btn-ghost p-1.5 rounded-full">
            <X size={18} />
          </button>
        </div>

        <div className="flex flex-col gap-4">
          <div>
            <label className="block text-xs font-medium text-slate-500 mb-1.5">Project name</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. Website Redesign"
              className="input"
              autoFocus
            />
          </div>
          <div>
            <label className="block text-xs font-medium text-slate-500 mb-1.5">Description (optional)</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="What's this project about?"
              className="input min-h-[80px] resize-none"
              rows={3}
            />
          </div>
          {error && (
            <p className="text-xs text-red-500">{error}</p>
          )}
          <div className="flex justify-end gap-2 pt-2">
            <button onClick={onClose} className="btn-ghost">Cancel</button>
            <button
              onClick={handleSubmit}
              disabled={!name.trim() || createProject.isPending}
              className="btn-primary disabled:opacity-50"
            >
              Create Project
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
