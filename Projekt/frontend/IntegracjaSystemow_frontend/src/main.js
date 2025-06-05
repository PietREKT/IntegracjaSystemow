import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from './router'
import './style.css'
import Toast, {POSITION} from "vue-toastification"
import "vue-toastification/dist/index.css"

const app = createApp(App)

const pinia = createPinia()
const options ={
    position: POSITION.BOTTOM_RIGHT
}

app.use(pinia)
app.use(router)
app.use(Toast, options)

app.mount('#app')