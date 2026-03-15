import { Trash2, GripVertical } from 'lucide-react'
import { StatusBadge, PriorityBadge } from './Badges'
import type { Task, TaskStatus } from '../utils/types'
import { useUpdateTask, useDeleteTask } from '../hooks/useTasks'

interface TaskCardProps {
  task: Task
}

const nextStatus: Record<TaskStatus, TaskStatus> = {
  todo: 'in_progress',
  in_progress: 'done',
  done: 'todo',
}

export default function TaskCard({ task }: TaskCardProps) {
  const updateTask = useUpdateTask()
  const deleteTask = useDeleteTask()

  const cycleStatus = () => {
    updateTask.mutate({ id: task.id, status: nextStatus[task.status] } as never)
  }

  return (
    <div className="card group flex items-start gap-3 hover:shadow-md transition-shadow">
      <button className="mt-1 cursor-grab text-slate-300 opacity-0 group-hover:opacity-100 transition-opacity">
        <GripVertical size={16} />
      </button>

      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 mb-1">
          <h3 className="text-sm font-semibold truncate">{task.title}</h3>
        </div>

        {task.description && (
          <p className="text-xs text-slate-400 truncate mb-2">{task.description}</p>
        )}

        <div className="flex items-center gap-2">
          <button onClick={cycleStatus} title="Click to cycle status">
            <StatusBadge status={task.status} />
          </button>
          <PriorityBadge priority={task.priority} />
        </div>
      </div>

      <button
        onClick={() => deleteTask.mutate(task.id)}
        className="mt-1 text-slate-300 opacity-0 group-hover:opacity-100 hover:text-danger transition-all"
        title="Delete task"
      >
        <Trash2 size={15} />
      </button>
    </div>
  )
}
