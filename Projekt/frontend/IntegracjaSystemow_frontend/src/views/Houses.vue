<template>
  <div>
    <h2 class="text-2xl font-semibold mb-4">Zestawienie mieszkań</h2>

    <div class="overflow-x-auto">
      <table class="table-fixed w-full bg-white rounded shadow">
        <thead>
        <tr>
          <th class="p-2 border w-32">Miasto</th>
          <th class="p-2 border w-32">Cena za m²</th>
          <th class="p-2 border w-32">Powierzchnia</th>
          <th class="p-2 border w-40">Data</th>
          <th class="p-2 border w-48">Typ</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="house in paginatedHouses" :key="house.id" class="h-12">
          <td class="p-2 border h-12 whitespace-nowrap text-center">{{ house.city }}</td>
          <td class="p-2 border h-12 text-center">{{ house.price_per_meter }}</td>
          <td class="p-2 border h-12 text-center">{{ house.area }}</td>
          <td class="p-2 border h-12 text-center">{{ house.date }}</td>
          <td class="p-2 border h-12 whitespace-nowrap text-center">{{ house.house_type }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="mt-4 flex justify-center gap-2">
      <button
          v-for="page in totalPages"
          :key="page"
          @click="currentPage = page"
          :class="[
          'px-3 py-1 border rounded',
          page === currentPage ? 'bg-blue-600 text-white' : 'bg-white'
        ]"
      >
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const houses = ref([
  { id: 1, city: 'Warszawa', price_per_meter: 10200, area: 50, date: '2024-01-01', house_type: 'APARTMENT_BUILDING' },
  { id: 2, city: 'Kraków', price_per_meter: 9500, area: 45, date: '2024-01-10', house_type: 'HOUSING_BLOCK' },
  { id: 3, city: 'Gdańsk', price_per_meter: 8800, area: 60, date: '2024-01-15', house_type: 'TENEMENT' },
])

const itemsPerPage = 2
const currentPage = ref(1)

const totalPages = computed(() => Math.ceil(houses.value.length / itemsPerPage))

const paginatedHouses = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return houses.value.slice(start, start + itemsPerPage)
})
</script>
