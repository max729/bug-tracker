import { createRouter, createWebHashHistory } from "vue-router";
import axios from "axios";
//import { useStore } from 'vuex';
import store from '../store/store.js';

import UserStats from "../views/UserStats.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Forgot from "../views/Forgot.vue";
import Reset from "../views/Reset.vue";
import Profile from "../views/Profile.vue";
import Tickets from "../views/Tickets.vue";
import Ticket from "../views/Ticket.vue"
import Projects from "../views/Projects.vue";
import Project from "../views/Project.vue"
import AppUsers from "../views/AppUsers.vue"


const routes = [
    {
        path: '/',
        name: 'Home',
        component: Login
    },  
    {
        path: '/userStats',
        name: 'UserStats',
        component: UserStats
    },  
    {
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
    },
    {
        path: '/appUsers',
        name: 'AppUsers',
        component: AppUsers
    }
]


const router = createRouter({
    history: createWebHashHistory(),
    routes
})








/* router.beforeEach(async (to,from,next)=>{
    
    const auth = store.state.auth//$store.state.auth;

    if( auth == true){
        next();
        return;
    }

    if(  localStorage.getItem("auth")==null || localStorage.getItem("auth")  == "false"        ){
        if(  to.path == '/register' || to.path == '/login' || to.path == '/forgot' || to.path == '/reset'  ){
            next();
            return
        } else {
            next({path: '/login'})
            return
        }


    }

    if( localStorage.getItem("auth") == "true" ){

        try{

            const { data } = await axios.get("/appUsers/fromToken");

            store.state.auth=true;
            store.state.user=data;
 

            next()
        } catch (e) {
            localStorage.setItem("auth","false");
            next({path: '/login'})
        }

    }

}) */

export default router;