<template>
  <div class="min-h-screen bg-gray-100">
    <header class="bg-white shadow p-4 flex justify-between items-center">
      <h1 class="text-xl font-bold">Ceny mieszkań i stopy procentowe</h1>
      <nav class="flex items-center gap-4">
        <RouterLink to="/" class="text-blue-600">Strona główna</RouterLink>
        <RouterLink to="/houses" class="text-blue-600">Mieszkania</RouterLink>
        <RouterLink to="/login" class="text-blue-600 font-semibold">Zaloguj się</RouterLink>
        <RouterLink to="/upload" class="text-blue-600">Dodaj dane</RouterLink>
      </nav>
    </header>

    <main class="p-4">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterView, RouterLink } from 'vue-router'
</script>
