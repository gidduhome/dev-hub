import { NavLink } from 'react-router-dom'
import { LayoutDashboard, FolderKanban, CheckSquare } from 'lucide-react'
import clsx from 'clsx'

const links = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/projects', label: 'Projects', icon: FolderKanban },
  { to: '/tasks', label: 'Tasks', icon: CheckSquare },
]

export default function Sidebar() {
  return (
    <aside className="fixed left-0 top-0 flex h-screen w-56 flex-col border-r border-slate-200 bg-white">
      <div className="flex h-16 items-center gap-2 px-5">
        <div className="h-8 w-8 rounded-lg bg-accent flex items-center justify-center">
          <CheckSquare size={18} className="text-white" />
        </div>
        <span className="font-display text-lg font-bold tracking-tight">TaskFlow</span>
      </div>

      <nav className="mt-4 flex flex-col gap-1 px-3">
        {links.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            end={to === '/'}
            className={({ isActive }) =>
              clsx(
                'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors',
                isActive
                  ? 'bg-accent/10 text-accent-dark'
                  : 'text-slate-500 hover:bg-slate-50 hover:text-ink',
              )
            }
          >
            <Icon size={18} />
            {label}
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
