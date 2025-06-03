<template>
  <div>
    <h2 class="text-2xl font-semibold mb-4">Zestawienie mieszkań</h2>

    <!-- Filtry -->
    <div class="mb-4 flex flex-wrap gap-4">
      <input v-model="filters.city" placeholder="Miasto" class="border p-2 rounded w-40" />
      <input v-model.number="filters.minPrice" placeholder="Min cena za m²" class="border p-2 rounded w-40" type="number" />
      <input v-model.number="filters.maxPrice" placeholder="Max cena za m²" class="border p-2 rounded w-40" type="number" />
      <input v-model.number="filters.minArea" placeholder="Min powierzchnia" class="border p-2 rounded w-40" type="number" />
      <input v-model.number="filters.maxArea" placeholder="Max powierzchnia" class="border p-2 rounded w-40" type="number" />
      <input v-model.number="filters.year" placeholder="Rok" class="border p-2 rounded w-32" type="number" />
    </div>

    <div class="overflow-x-auto">
      <table class="table-fixed w-full bg-white rounded shadow">
        <thead>
        <tr>
          <th class="p-2 border w-32">Miasto</th>
          <th class="p-2 border w-32">Cena za m²</th>
          <th class="p-2 border w-32">Powierzchnia</th>
          <th class="p-2 border w-32">Rok</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="house in paginatedHouses" :key="house.id">
          <td class="p-2 border text-center">{{ house.city }}</td>
          <td class="p-2 border text-center">{{ house.pricePerMeter?.toFixed(2) ?? '-' }}</td>
          <td class="p-2 border text-center">{{ house.area ?? '-' }}</td>
          <td class="p-2 border text-center">{{ house.transactionYear ?? '-' }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Paginacja -->
    <div class="mt-4 flex justify-center gap-1 flex-wrap">
      <button class="px-2 py-1 border rounded" :disabled="currentPage === 0" @click="goToPage(0)">«</button>
      <button class="px-2 py-1 border rounded" :disabled="currentPage === 0" @click="goToPage(currentPage - 1)">‹</button>
      <button
          v-for="page in visiblePageNumbers"
          :key="page"
          @click="goToPage(page)"
          :class="[ 'px-3 py-1 border rounded', page === currentPage ? 'bg-blue-600 text-white' : 'bg-white' ]"
      >
        {{ page + 1 }}
      </button>
      <button class="px-2 py-1 border rounded" :disabled="currentPage === totalPages - 1" @click="goToPage(currentPage + 1)">›</button>
      <button class="px-2 py-1 border rounded" :disabled="currentPage === totalPages - 1" @click="goToPage(totalPages - 1)">»</button>
    </div>

    <div class="mt-4 flex justify-center items-center gap-2">
      <label for="jump" class="text-sm">Idź do strony:</label>
      <input
          id="jump"
          type="number"
          min="1"
          :max="totalPages"
          v-model.number="jumpPage"
          @keyup.enter="goToPage(jumpPage - 1)"
          class="border px-2 py-1 rounded w-20"
      />
      <button @click="goToPage(jumpPage - 1)" class="px-3 py-1 border rounded bg-blue-600 text-white">
        Idź
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const rawHouses = ref([])
const filters = ref({ city: '', minPrice: null, maxPrice: null, minArea: null, maxArea: null, year: null })
const currentPage = ref(0)
const itemsPerPage = 10
const jumpPage = ref(1)

onMounted(async () => {
  const res = await axios.get('http://localhost:8080/api/houses', {
    params: { page: 0, elementsPerPage: 10000 }
  })
  rawHouses.value = res.data.content.map((h) => ({
    id: h.id,
    city: h.city,
    pricePerMeter: h.pricePerMeter ?? 0,
    area: h.area ?? null,
    transactionYear: h.transactionYear ?? null
  }))
})

const filteredHouses = computed(() => {
  return rawHouses.value.filter(h => {
    if (filters.value.city && !h.city.toLowerCase().includes(filters.value.city.toLowerCase())) return false
    if (filters.value.minPrice !== null && h.pricePerMeter < filters.value.minPrice) return false
    if (filters.value.maxPrice !== null && h.pricePerMeter > filters.value.maxPrice) return false
    if (filters.value.minArea !== null && (h.area === null || h.area < filters.value.minArea)) return false
    if (filters.value.maxArea !== null && (h.area === null || h.area > filters.value.maxArea)) return false
    if (filters.value.year !== null && h.transactionYear !== filters.value.year) return false
    return true
  })
})

const totalPages = computed(() => Math.ceil(filteredHouses.value.length / itemsPerPage))

const paginatedHouses = computed(() => {
  const start = currentPage.value * itemsPerPage
  return filteredHouses.value.slice(start, start + itemsPerPage)
})

const maxVisiblePages = 9
const visiblePageNumbers = computed(() => {
  const pages = []
  let start = Math.max(currentPage.value - Math.floor(maxVisiblePages / 2), 0)
  let end = Math.min(start + maxVisiblePages, totalPages.value)
  if (end - start < maxVisiblePages) start = Math.max(end - maxVisiblePages, 0)
  for (let i = start; i < end; i++) pages.push(i)
  return pages
})

function goToPage(page) {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page
    jumpPage.value = page + 1
  }
}
</script>

<style scoped>
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>
