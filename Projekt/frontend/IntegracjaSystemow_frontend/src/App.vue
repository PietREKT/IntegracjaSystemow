<template>
  <div class="min-h-screen bg-gray-100">
    <header class="bg-white shadow p-4 flex justify-between items-center">
      <h1 class="text-xl font-bold">Ceny mieszkań i stopy procentowe</h1>
      <nav class="flex items-center gap-4">
        <RouterLink to="/" class="text-blue-600">Strona główna</RouterLink>
        <RouterLink to="/houses" class="text-blue-600">Mieszkania</RouterLink>
        <RouterLink to="/upload" class="text-blue-600">Dodaj dane</RouterLink>

        <template v-if="isLoggedIn">
          <button @click="logoutAndRedirect" class="text-red-600 font-semibold hover:underline">Wyloguj się</button>
        </template>
        <template v-else>
          <RouterLink to="/login" class="text-blue-600 font-semibold">Zaloguj się</RouterLink>
        </template>
      </nav>
    </header>

    <main class="px-4 py-2">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterView, RouterLink, useRouter } from 'vue-router'
import { useAuth } from './api/useAuth.js'

const { isLoggedIn, logout } = useAuth()
const router = useRouter()

function logoutAndRedirect() {
  logout()
  router.push('/')
}
</script>

