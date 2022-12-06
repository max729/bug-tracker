import { createApp } from 'vue'
import '../scss/style.scss'
import * as bootstrap from 'bootstrap'
import App from './App.vue'
import router from './router/router.js'
import refreshToken from './interceptors/refreshToken.js'
import store from './store/store.js'

createApp(App)
.use(store)
.use(router)
.mount('#app')
