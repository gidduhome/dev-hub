import { Link } from 'react-router-dom'
import { FolderKanban, CheckSquare, Clock, CheckCircle } from 'lucide-react'
import { useProjects } from '../hooks/useProjects'
import { useTasks } from '../hooks/useTasks'

export default function DashboardPage() {
  const { data: projectsRes } = useProjects()
  const { data: allTasks } = useTasks()
  const { data: inProgress } = useTasks({ status_filter: 'in_progress' })
  const { data: done } = useTasks({ status_filter: 'done' })

  const stats = [
    { label: 'Projects', value: projectsRes?.meta?.total ?? 0, icon: FolderKanban, color: 'bg-violet-100 text-violet-600' },
    { label: 'Total Tasks', value: allTasks?.meta?.total ?? 0, icon: CheckSquare, color: 'bg-sky-100 text-sky-600' },
    { label: 'In Progress', value: inProgress?.meta?.total ?? 0, icon: Clock, color: 'bg-amber-100 text-amber-600' },
    { label: 'Completed', value: done?.meta?.total ?? 0, icon: CheckCircle, color: 'bg-emerald-100 text-emerald-600' },
  ]

  const recentProjects = projectsRes?.data?.slice(0, 5) ?? []

  return (
    <div>
      <h1 className="font-display text-2xl font-bold mb-6">Dashboard</h1>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        {stats.map((s) => (
          <div key={s.label} className="card flex items-center gap-4">
            <div className={`flex h-10 w-10 items-center justify-center rounded-lg ${s.color}`}>
              <s.icon size={20} />
            </div>
            <div>
              <p className="text-2xl font-bold">{s.value}</p>
              <p className="text-xs text-slate-400">{s.label}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h2 className="font-display text-base font-bold">Recent Projects</h2>
          <Link to="/projects" className="text-xs text-accent font-medium hover:underline">
            View all
          </Link>
        </div>
        {recentProjects.length === 0 ? (
          <p className="text-sm text-slate-400">No projects yet. Create one to get started.</p>
        ) : (
          <div className="divide-y divide-slate-100">
            {recentProjects.map((p) => (
              <Link
                key={p.id}
                to={`/projects/${p.id}`}
                className="flex items-center justify-between py-3 hover:bg-slate-50 -mx-5 px-5 transition-colors"
              >
                <div>
                  <p className="text-sm font-semibold">{p.name}</p>
                  {p.description && <p className="text-xs text-slate-400 truncate max-w-md">{p.description}</p>}
                </div>
                <span className="badge bg-slate-100 text-slate-500">{p.task_count} tasks</span>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
