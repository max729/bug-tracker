import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'



// https://vitejs.dev/config/
export default defineConfig({
  root: path.resolve(__dirname, 'src/main/resources/templates'),
  resolve: {
    alias: {
      '~bootstrap': path.resolve(__dirname, 'node_modules/bootstrap'),
      '@' : path.resolve(__dirname, 'src/main/resources')
    }
  },
  build: {
    outDir: path.resolve(__dirname, 'target/classes/templates'),
        manifest: true
  },
  server: {
    port: 8081,
    hot: true
  },
  plugins: [vue()]
})
