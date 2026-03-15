import { useParams, Link } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import { useProject } from '../hooks/useProjects'
import { useTasks } from '../hooks/useTasks'
import TaskCard from '../components/TaskCard'
import CreateTaskForm from '../components/CreateTaskForm'

export default function ProjectDetailPage() {
  const { id } = useParams<{ id: string }>()
  const projectId = Number(id)
  const { data: projectRes, isLoading: loadingProject } = useProject(projectId)
  const { data: tasksRes, isLoading: loadingTasks } = useTasks({ project_id: projectId })

  const project = projectRes?.data
  const tasks = tasksRes?.data ?? []

  if (loadingProject) return <p className="text-sm text-slate-400">Loading...</p>
  if (!project) return <p className="text-sm text-danger">Project not found.</p>

  const todoTasks = tasks.filter((t) => t.status === 'todo')
  const inProgressTasks = tasks.filter((t) => t.status === 'in_progress')
  const doneTasks = tasks.filter((t) => t.status === 'done')

  const columns = [
    { title: 'To Do', tasks: todoTasks, color: 'border-slate-300' },
    { title: 'In Progress', tasks: inProgressTasks, color: 'border-amber-400' },
    { title: 'Done', tasks: doneTasks, color: 'border-emerald-400' },
  ]

  return (
    <div>
      <Link to="/projects" className="btn-ghost mb-4 inline-flex">
        <ArrowLeft size={16} />
        Back to projects
      </Link>

      <div className="mb-6">
        <h1 className="font-display text-2xl font-bold">{project.name}</h1>
        {project.description && (
          <p className="text-sm text-slate-400 mt-1">{project.description}</p>
        )}
      </div>

      <div className="mb-6">
        <CreateTaskForm projectId={projectId} />
      </div>

      {loadingTasks ? (
        <p className="text-sm text-slate-400">Loading tasks...</p>
      ) : tasks.length === 0 ? (
        <p className="text-sm text-slate-400">No tasks yet. Add one above.</p>
      ) : (
        <div className="grid gap-6 lg:grid-cols-3">
          {columns.map((col) => (
            <div key={col.title}>
              <div className={`flex items-center gap-2 mb-3 border-b-2 ${col.color} pb-2`}>
                <h2 className="text-xs font-bold uppercase tracking-wider text-slate-500">
                  {col.title}
                </h2>
                <span className="badge bg-slate-100 text-slate-500">{col.tasks.length}</span>
              </div>
              <div className="flex flex-col gap-3">
                {col.tasks.map((task) => (
                  <TaskCard key={task.id} task={task} />
                ))}
                {col.tasks.length === 0 && (
                  <p className="text-xs text-slate-300 text-center py-8">No tasks</p>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
