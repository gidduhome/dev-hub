/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ['"DM Sans"', 'system-ui', 'sans-serif'],
        display: ['"Outfit"', 'system-ui', 'sans-serif'],
      },
      colors: {
        ink: '#1a1a2e',
        slate: { 50: '#f8fafc', 100: '#f1f5f9', 200: '#e2e8f0', 300: '#cbd5e1', 400: '#94a3b8', 500: '#64748b' },
        accent: { DEFAULT: '#f97316', light: '#fdba74', dark: '#c2410c' },
        success: '#22c55e',
        danger: '#ef4444',
      },
    },
  },
  plugins: [],
}
