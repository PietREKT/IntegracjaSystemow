import { ref, computed } from 'vue'

const token = ref(localStorage.getItem('token'))

export function useAuth() {
    const isLoggedIn = computed(() => !!token.value)

    function login(newToken) {
        token.value = newToken
        localStorage.setItem('token', newToken)
    }

    function logout() {
        token.value = null
        localStorage.removeItem('token')
    }

    return { token, isLoggedIn, login, logout }
}
