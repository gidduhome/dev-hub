import { useState } from 'react'
import { Plus } from 'lucide-react'
import { useCreateTask } from '../hooks/useTasks'
import type { TaskPriority } from '../utils/types'

interface CreateTaskFormProps {
  projectId: number
}

export default function CreateTaskForm({ projectId }: CreateTaskFormProps) {
  const [title, setTitle] = useState('')
  const [priority, setPriority] = useState<TaskPriority>('medium')
  const createTask = useCreateTask()

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!title.trim()) return
    createTask.mutate(
      { title: title.trim(), priority, project_id: projectId },
      { onSuccess: () => setTitle('') },
    )
  }

  return (
    <form onSubmit={handleSubmit} className="flex items-center gap-2">
      <input
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="Add a new task..."
        className="input flex-1"
      />
      <select
        value={priority}
        onChange={(e) => setPriority(e.target.value as TaskPriority)}
        className="input w-28"
      >
        <option value="low">Low</option>
        <option value="medium">Medium</option>
        <option value="high">High</option>
      </select>
      <button
        type="submit"
        disabled={!title.trim() || createTask.isPending}
        className="btn-primary disabled:opacity-50"
      >
        <Plus size={16} />
        Add
      </button>
    </form>
  )
}
