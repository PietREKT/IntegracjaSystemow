<template>
  <div>
    <h2 class="text-2xl font-semibold mb-4">Dodaj nowe mieszkanie</h2>
    <form @submit.prevent="submitForm" class="bg-white p-6 rounded shadow w-full max-w-xl space-y-4">
      <input v-model="form.city" type="text" placeholder="Miasto" class="w-full border p-2" required />
      <input v-model.number="form.price_per_meter" type="number" placeholder="Cena za m²" class="w-full border p-2" required />
      <input v-model.number="form.area" type="number" placeholder="Powierzchnia (m²)" class="w-full border p-2" required />
      <input v-model="form.date" type="date" class="w-full border p-2" required />
      <select v-model="form.house_type" class="w-full border p-2" required>
        <option disabled value="">Wybierz typ</option>
        <option value="HOUSING_BLOCK">Blok mieszkalny</option>
        <option value="APARTMENT_BUILDING">Budynek apartamentowy</option>
        <option value="TENEMENT">Kamienica</option>
      </select>
      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Dodaj</button>
    </form>

    <div v-if="added" class="mt-4 text-green-700 font-semibold">Dodano mieszkanie!</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const form = ref({
  city: '',
  price_per_meter: '',
  area: '',
  date: '',
  house_type: ''
})

const added = ref(false)

function submitForm() {
  console.log('Nowe mieszkanie:', form.value)
  added.value = true
  form.value = { city: '', price_per_meter: '', area: '', date: '', house_type: '' }
}
</script>
