<template>
  <div class="flex flex-col items-center justify-center min-h-[60vh]">
    <div class="bg-white p-6 rounded shadow w-full max-w-sm">
      <h2 class="text-2xl font-semibold mb-4">Zaloguj się</h2>
      <form @submit.prevent="loginHandler" class="space-y-4">
      <div>
          <label>Nazwa użytkownika</label>
          <input v-model="username" type="text" placeholder="Nazwa użytkownika" class="w-full border p-2 mt-1" required />
        </div>
        <div>
          <label>Hasło</label>
          <input v-model="password" type="password" placeholder="Hasło" class="w-full border p-2 mt-1" required />
        </div>
        <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded w-full">Zaloguj się</button>
      </form>

      <div class="mt-4 text-center">
        <span class="text-sm text-gray-600">Nie masz konta?</span>
        <RouterLink to="/register" class="text-blue-600 font-medium ml-1 hover:underline">Zarejestruj się</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '../api/auth.js'
import { useAuth } from '../api/useAuth.js'

const username = ref('')
const password = ref('')
const router = useRouter()
const { login } = useAuth()

async function loginHandler() {
  try {
    const token = await loginUser(username.value, password.value)
    login(token)
    alert('Zalogowano pomyślnie!')
    router.push('/') // <-- przekierowanie na stronę główną
  } catch (error) {
    alert('Błąd logowania: ' + (error.response?.data || error.message))
  }
}
</script>

