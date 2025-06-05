<template>
  <div class="flex justify-center items-start min-h-[50vh]">
    <form @submit.prevent="register" class="bg-white p-6 rounded shadow w-full max-w-md mt-10">
      <h2 class="text-2xl font-semibold mb-4">Rejestracja</h2>

      <input v-model="name" type="text" placeholder="Nazwa użytkownika" class="border p-2 w-full mb-4" required />
      <input v-model="password" type="password" placeholder="Hasło" class="border p-2 w-full mb-4" required />
      <input v-model="confirmPassword" type="password" placeholder="Powtórz hasło" class="border p-2 w-full mb-4" required />

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded w-full">Zarejestruj się</button>
    </form>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerUser } from '../api/auth.js'
import Note from "../note.js";
import {useAuth} from "../api/useAuth.js";

const name = ref('')
const password = ref('')
const confirmPassword = ref('')
const router = useRouter()
const { login } = useAuth()

async function register() {
  if (password.value !== confirmPassword.value) {
    Note().error('Hasła się nie zgadzają!')
    return
  }

  try {
    let token = await registerUser(name.value, password.value)
    login(token)
    Note().success('Rejestracja zakończona sukcesem!')
    await router.push('/')
  } catch (error) {
    Note().error('Błąd rejestracji: ' + (error.response?.data || error.message))
  }
}
</script>

