import clsx from 'clsx'
import type { TaskStatus, TaskPriority } from '../utils/types'

const statusStyles: Record<TaskStatus, string> = {
  todo: 'bg-slate-100 text-slate-600',
  in_progress: 'bg-amber-100 text-amber-700',
  done: 'bg-emerald-100 text-emerald-700',
}

const statusLabels: Record<TaskStatus, string> = {
  todo: 'To Do',
  in_progress: 'In Progress',
  done: 'Done',
}

const priorityStyles: Record<TaskPriority, string> = {
  low: 'bg-sky-100 text-sky-700',
  medium: 'bg-orange-100 text-orange-700',
  high: 'bg-red-100 text-red-700',
}

export function StatusBadge({ status }: { status: TaskStatus }) {
  return <span className={clsx('badge', statusStyles[status])}>{statusLabels[status]}</span>
}

export function PriorityBadge({ priority }: { priority: TaskPriority }) {
  return <span className={clsx('badge capitalize', priorityStyles[priority])}>{priority}</span>
}
