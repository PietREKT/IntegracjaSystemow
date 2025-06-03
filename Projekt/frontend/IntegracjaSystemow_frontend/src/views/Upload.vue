<template>
  <div class="flex justify-center items-start min-h-screen bg-gray-100 pt-10">
    <div class="bg-white p-6 rounded shadow w-full max-w-xl">
      <h2 class="text-2xl font-semibold mb-4">Dodaj nowe mieszkanie</h2>
      <form @submit.prevent="submitForm" class="space-y-4">
        <input v-model="form.city" type="text" placeholder="Miasto" class="w-full border p-2" required />
        <input v-model.number="form.price_per_meter" type="number" placeholder="Cena za m²" class="w-full border p-2" required />
        <input v-model.number="form.area" type="number" placeholder="Powierzchnia (m²)" class="w-full border p-2" required />
        <input v-model="form.year" type="text" placeholder="Rok (np. 2022)" class="w-full border p-2" required />
        <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded w-full">Dodaj</button>
      </form>
      <div v-if="added" class="mt-4 text-green-700 font-semibold">Dodano mieszkanie!</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { addHouse } from '../api/houses.js'

const form = ref({
  city: '',
  price_per_meter: '',
  area: '',
  year: ''
})

const added = ref(false)

async function submitForm() {
  const token = localStorage.getItem('token')

  if (!token) {
    alert('Musisz być zalogowany, aby dodać mieszkanie!')
    return
  }

  try {
    await addHouse(form.value)
    added.value = true
    form.value = {
      city: '',
      price_per_meter: '',
      area: '',
      year: ''
    }
  } catch (error) {
    alert('Błąd dodawania: ' + (error.response?.data || error.message))
  }
}
</script>



