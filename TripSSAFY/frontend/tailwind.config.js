/** @type {import('tailwindcss').Config} */

import { defineConfig } from 'vite'
import path from 'path'

export default {
  content: ['./index.html', './src/**/*.{vue,js,ts}'],
  theme: {
    extend: {
      colors: {
        ssafy_blue: '#0055FB',
        ssafy_orange: '#FBA600'
      },
      fontFamily: {
        sans: ['Pretandard', 'sans-serif'],  // 기본 sans 글꼴 지정
      },
    },
  },
  plugins: [],
}