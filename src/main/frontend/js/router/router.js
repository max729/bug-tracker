import { createRouter, createWebHashHistory } from "vue-router";

import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Forgot from "../views/Forgot.vue";
import Reset from "../views/Reset.vue";
import Profile from "../views/Profile.vue";
import Tickets from "../views/Tickets.vue";
import Ticket from "../views/Ticket.vue"
import Projects from "../views/Projects.vue";
import Project from "../views/Project.vue"


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
        path: '/ticket/:id',
        name: 'Ticket',
        component: Ticket
    },
    {
        path: '/projects',
        name: 'Projects',
        component: Projects
    },
    {
        path: '/project/:id',
        name: 'Project',
        component: Project
    }
]


const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router;