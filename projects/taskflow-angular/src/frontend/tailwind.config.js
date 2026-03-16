/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        ink: '#1a1a2e',
        accent: '#f97316',
        success: '#22c55e',
        danger: '#ef4444',
        slate: {
          800: '#1e293b',
          700: '#334155',
          600: '#475569',
          500: '#64748b',
          400: '#94a3b8',
          300: '#cbd5e1',
          200: '#e2e8f0',
          100: '#f1f5f9',
          50: '#f8fafc'
        }
      },
      fontFamily: {
        sans: ['DM Sans', 'sans-serif'],
        display: ['Outfit', 'sans-serif'],
      }
    }
  },
  plugins: []
}
