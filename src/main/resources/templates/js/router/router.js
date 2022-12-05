import { createRouter, createWebHashHistory } from "vue-router";
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/',
        name: 'Home',
        component: Home
    }
]


const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router;