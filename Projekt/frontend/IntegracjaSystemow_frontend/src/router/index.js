import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Dashboard.vue'
import Houses from '../views/Houses.vue'
import Login from '../views/Login.vue'

const routes = [
    { path: '/', component: Home, name: 'Home' },
    { path: '/houses', component: Houses },
    { path: '/login', component: Login },
    { path: '/upload', component: () => import('../views/Upload.vue')},
    { path: '/register', component: () => import('../views/Register.vue'),},

]

export default createRouter({
    history: createWebHistory(),
    routes
})
