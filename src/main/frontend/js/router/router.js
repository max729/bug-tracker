import { createRouter, createWebHashHistory } from "vue-router";

import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Forgot from "../views/Forgot.vue";
import Reset from "../views/Reset.vue";
import Profile from "../views/Profile.vue";
import Tickets from "../views/Tickets.vue";
import Projects from "../views/Projects.vue";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Login
    },  {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/forgot',
        name: 'Forgot',
        component: Forgot
    },
    {
        path: '/reset/:token',
        name: 'Reset',
        component: Reset
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/tickets',
        name: 'Tickets',
        component: Tickets
    },
    {
        path: '/projects',
        name: 'Projects',
        component: Projects
    }
]


const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router;