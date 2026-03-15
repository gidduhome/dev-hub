# Frontend: TaskFlow UI

## Conventions

- Components: PascalCase (`TaskCard.tsx`)
- Hooks: camelCase with `use` prefix (`useTasks.ts`)
- Pages in `/pages`, reusable pieces in `/components`
- API calls only through `/services/api.ts` — never inline fetch
- Functional components + hooks only, no class components
- `interface` for props, `type` for unions

## Key Files

- `src/App.tsx` → Router + layout shell
- `src/services/api.ts` → Axios instance with interceptors
- `src/hooks/useTasks.ts` → React Query hooks for tasks
- `src/hooks/useProjects.ts` → React Query hooks for projects
