/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./public/index.html", "./src/**/*.{js,jsx,ts,tsx,css,html}",
  ],
  theme: {
    extend: {
      fontFamily: {
        // Custom fonts
        sans: ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', 'sans-serif'],
        code: ['source-code-pro', 'Menlo', 'Monaco', 'Consolas', 'Courier New', 'monospace'],
      },
      // Add custom utilities for font smoothing
      screens: {
        'font-smoothing': {
          '-webkit-font-smoothing': 'antialiased',
          '-moz-osx-font-smoothing': 'grayscale',
        }
      },
    },
  },
  plugins: [],
}
