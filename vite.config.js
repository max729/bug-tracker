import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'


// https://vitejs.dev/config/
export default defineConfig({
  root: path.resolve(__dirname, 'src/main/frontend'),
  resolve: {
    alias: {
      '~bootstrap': path.resolve(__dirname, 'node_modules/bootstrap'),
      '@' : path.resolve(__dirname, 'src/main/frontend')
    }
  },
  build: {
    outDir: path.resolve(__dirname, 'src/main/resources/static')
  },
  server: {
    port: 8081,
    hot: true
  },
  plugins: [vue()]
})
