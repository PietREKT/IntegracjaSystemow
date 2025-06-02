<template>
  <div class="flex justify-center items-start min-h-[50vh]">
    <form @submit.prevent="register" class="bg-white p-6 rounded shadow w-full max-w-md mt-10">
      <h2 class="text-2xl font-semibold mb-4">Rejestracja</h2>

      <input v-model="name" type="text" placeholder="Imię i nazwisko" class="border p-2 w-full mb-4" />
      <input v-model="email" type="email" placeholder="Email" class="border p-2 w-full mb-4" />
      <input v-model="password" type="password" placeholder="Hasło" class="border p-2 w-full mb-4" />
      <input v-model="confirmPassword" type="password" placeholder="Powtórz hasło" class="border p-2 w-full mb-4" />

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Zarejestruj się</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')

function register() {
  if (password.value !== confirmPassword.value) {
    alert('Hasła się nie zgadzają!')
    return
  }
  alert(`Zarejestrowano użytkownika: ${name.value} (${email.value})`)
}
</script>

