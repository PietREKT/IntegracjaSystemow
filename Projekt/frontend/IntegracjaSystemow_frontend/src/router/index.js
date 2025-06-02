import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Dashboard.vue'
import Houses from '../views/Houses.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'

const routes = [
    { path: '/', component: Home, name: 'Home' },
    { path: '/houses', component: Houses },
    { path: '/login', component: Login },
    { path: '/upload', component: () => import('../views/Upload.vue')},
    { path: '/register', component: Register }
]

export default createRouter({
    history: createWebHistory(),
    routes
})
