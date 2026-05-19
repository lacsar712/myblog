/** @type {import('tailwindcss').Config} */

export default {
  darkMode: "class",
  content: ["./index.html", "./src/**/*.{js,ts,vue}"],
  theme: {
    container: {
      center: true,
      padding: '1rem',
    },
    extend: {
      colors: {
        background: "var(--bg)",
        foreground: "var(--text)",
        muted: "var(--muted)",
        border: "var(--border)",
        primary: "var(--primary)",
        danger: "var(--danger)",
      },
      fontFamily: {
        sans: ['"Inter"', 'ui-sans-serif', 'system-ui', 'sans-serif'],
        mono: ['"Fira Code"', 'ui-monospace', 'SFMono-Regular', 'monospace'],
      }
    },
  },
  plugins: [],
};
