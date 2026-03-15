import { useState } from 'react'
import { Search } from 'lucide-react'
import { useTasks } from '../hooks/useTasks'
import TaskCard from '../components/TaskCard'
import type { TaskStatus, TaskPriority } from '../utils/types'

export default function TasksPage() {
  const [search, setSearch] = useState('')
  const [statusFilter, setStatusFilter] = useState<TaskStatus | ''>('')
  const [priorityFilter, setPriorityFilter] = useState<TaskPriority | ''>('')

  const { data: res, isLoading } = useTasks({
    search: search || undefined,
    status_filter: statusFilter || undefined,
    priority: priorityFilter || undefined,
  })

  const tasks = res?.data ?? []

  return (
    <div>
      <h1 className="font-display text-2xl font-bold mb-6">All Tasks</h1>

      <div className="flex flex-wrap items-center gap-3 mb-6">
        <div className="relative flex-1 min-w-[200px]">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search tasks..."
            className="input pl-9"
          />
        </div>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as TaskStatus | '')}
          className="input w-36"
        >
          <option value="">All statuses</option>
          <option value="todo">To Do</option>
          <option value="in_progress">In Progress</option>
          <option value="done">Done</option>
        </select>
        <select
          value={priorityFilter}
          onChange={(e) => setPriorityFilter(e.target.value as TaskPriority | '')}
          className="input w-36"
        >
          <option value="">All priorities</option>
          <option value="high">High</option>
          <option value="medium">Medium</option>
          <option value="low">Low</option>
        </select>
      </div>

      {isLoading ? (
        <p className="text-sm text-slate-400">Loading...</p>
      ) : tasks.length === 0 ? (
        <div className="card py-16 text-center">
          <p className="text-sm text-slate-400">
            {search || statusFilter || priorityFilter
              ? 'No tasks match your filters.'
              : 'No tasks yet. Create a project and add tasks to it.'}
          </p>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {tasks.map((task) => (
            <TaskCard key={task.id} task={task} />
          ))}
        </div>
      )}

      {(res?.meta?.total ?? 0) > 0 && (
        <p className="text-xs text-slate-400 mt-4 text-center">
          Showing {tasks.length} of {res?.meta?.total} tasks
        </p>
      )}
    </div>
  )
}
