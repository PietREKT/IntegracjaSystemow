<template>
  <div class="bg-white rounded-xl shadow-lg p-6">
    <h2 class="text-2xl md:text-3xl font-bold text-gray-800 mb-6 pb-2 border-b border-gray-100">Zestawienie
      mieszkań</h2>

    <div class="overflow-x-auto rounded-lg border border-gray-200">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
          <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider cursor-pointer group"
              @click="sort('city')"
          >
            <div class="flex items-center">
              Miasto
              <svg v-if="filters.sortBy === 'city'" class="w-4 h-4 ml-1"
                   :class="{'rotate-180': filters.order === 'asc'}" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                      clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="h-0.5 bg-transparent group-hover:bg-blue-400 transition-all mt-1 w-3/4"></div>
          </th>
          <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider cursor-pointer group"
              @click="sort('pricePerMeter')"
          >
            <div class="flex items-center">
              Cena za m²
              <svg v-if="filters.sortBy === 'pricePerMeter'" class="w-4 h-4 ml-1"
                   :class="{'rotate-180': filters.order === 'asc'}" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                      clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="h-0.5 bg-transparent group-hover:bg-blue-400 transition-all mt-1 w-3/4"></div>
          </th>
          <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider cursor-pointer group"
              @click="sort('area')"
          >
            <div class="flex items-center">
              Powierzchnia
              <svg v-if="filters.sortBy === 'area'" class="w-4 h-4 ml-1"
                   :class="{'rotate-180': filters.order === 'asc'}" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                      clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="h-0.5 bg-transparent group-hover:bg-blue-400 transition-all mt-1 w-3/4"></div>
          </th>
          <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider cursor-pointer group"
              @click="sort('transactionYear')"
              colspan="2"
          >
            <div class="flex items-center">
              Rok
              <svg v-if="filters.sortBy === 'transactionYear'" class="w-4 h-4 ml-1"
                   :class="{'rotate-180': filters.order === 'asc'}" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd"
                      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                      clip-rule="evenodd"/>
              </svg>
            </div>
            <div class="h-0.5 bg-transparent group-hover:bg-blue-400 transition-all mt-1 w-3/4"></div>
          </th>
        </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
        <tr
            v-for="house in houses"
            :key="house.id"
            class="hover:bg-blue-50 transition-colors duration-150"
        >
          <td class="px-4 py-3 whitespace-nowrap">
            <div class="flex items-center">
              <div class="w-2 h-2 rounded-full bg-blue-500 mr-2"></div>
              <span class="text-sm font-medium text-gray-900">{{ house.city }}</span>
            </div>
          </td>
          <td class="px-4 py-3 whitespace-nowrap">
            <span class="text-sm text-gray-700">{{ house.pricePerMeter?.toFixed(2) ?? '-' }} zł</span>
          </td>
          <td class="px-4 py-3 whitespace-nowrap">
            <span class="text-sm text-gray-700">{{ house.area ?? '-' }} m²</span>
          </td>
          <td class="px-4 py-3 whitespace-nowrap">
              <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                {{ house.transactionYear ?? '-' }}
              </span>
          </td>
          <template v-if="house.user !== null">
            <td>
              <button @click="deleteHouse(house.id)"
                      class="px-2 py-1 text-sm bg-red-500 text-white rounded hover:bg-red-600 cursor-pointer"
              >
                Delete
              </button>
            </td>
          </template>
          <template v-else>
          </template>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="mt-6 flex flex-col sm:flex-row justify-between items-center space-y-4 sm:space-y-0">
      <div class="flex items-center">
        <span class="text-sm text-gray-700 mr-3">Pozycje na stronie:</span>
        <select
            id="elementsPerPage"
            v-model.number="filters.elementsPerPage"
            class="pl-3 pr-8 py-1.5 text-sm border border-gray-300 rounded-lg bg-white focus:ring-blue-500 focus:border-blue-500"
        >
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
          <option :value="100">100</option>
        </select>
      </div>

      <div class="flex items-center space-x-1">
        <button
            :disabled="page === 0"
            @click="changePage(0)"
            class="p-2 rounded-lg border border-gray-300 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 19l-7-7 7-7m8 14l-7-7 7-7"/>
          </svg>
        </button>

        <button
            :disabled="page === 0"
            @click="changePage(page - 1)"
            class="p-2 rounded-lg border border-gray-300 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>

        <div class="flex space-x-1">
          <button
              v-for="p in paginationRange"
              :key="p"
              :disabled="p === '...'"
              @click="typeof p === 'number' && changePage(p)"
              :class="[
              'w-9 h-9 text-sm rounded-lg border',
              p === page
                ? 'bg-blue-600 text-white border-blue-600 font-medium'
                : 'bg-white border-gray-300 text-gray-700 hover:bg-gray-50'
            ]"
          >
            {{ p === '...' ? '...' : p + 1 }}
          </button>
        </div>

        <button
            :disabled="page === totalPages - 1"
            @click="changePage(page + 1)"
            class="p-2 rounded-lg border border-gray-300 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </button>

        <button
            :disabled="page === totalPages - 1"
            @click="changePage(totalPages - 1)"
            class="p-2 rounded-lg border border-gray-300 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 5l7 7-7 7M5 5l7 7-7 7"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, watch, computed} from 'vue'
import axios from 'axios'

const houses = ref([])
const page = ref(0)
const totalPages = ref(0)

const filters = ref({
  elementsPerPage: 10,
  sortBy: 'pricePerMeter',
  order: 'desc'
})

async function fetchHouses() {
  try {
    const res = await axios.get('http://localhost:8080/api/houses', {
      headers: {
        Authorization: ('Bearer ' + localStorage.getItem('token')) || ''
      },
      params: {
        page: page.value,
        elementsPerPage: filters.value.elementsPerPage,
        sorting: filters.value.sortBy || undefined,
        order: filters.value.sortBy ? filters.value.order : undefined
      }
    })
    houses.value = res.data.content
    totalPages.value = res.data.totalPages
  } catch (e) {
    console.error('Błąd przy pobieraniu danych:', e)
  }
}

async function deleteHouse(id){
  await axios.delete("http://localhost:8080/api/houses/delete/" + id, {
    headers: {
      Authorization: ('Bearer ' + localStorage.getItem('token')) || ''
    }
  });
  await fetchHouses();
}

function sort(column) {
  if (filters.value.sortBy === column) {
    filters.value.order = filters.value.order === 'asc' ? 'desc' : 'asc'
  } else {
    filters.value.sortBy = column
    filters.value.order = 'asc'
  }
  page.value = 0
  fetchHouses()
}

function changePage(newPage) {
  if (newPage >= 0 && newPage < totalPages.value) {
    page.value = newPage
    fetchHouses()
  }
}

watch(() => filters.value.elementsPerPage, () => {
  page.value = 0
  fetchHouses()
})

onMounted(fetchHouses)

// Pagination range calculation
const paginationRange = computed(() => {
  const total = totalPages.value
  const current = page.value
  const delta = 2
  const range = []
  const rangeWithDots = []
  let l

  for (let i = 0; i < total; i++) {
    if (i === 0 || i === total - 1 || (i >= current - delta && i <= current + delta)) {
      range.push(i)
    }
  }

  for (let i of range) {
    if (l !== undefined) {
      if (i - l === 2) {
        rangeWithDots.push(l + 1)
      } else if (i - l > 2) {
        rangeWithDots.push('...')
      }
    }
    rangeWithDots.push(i)
    l = i
  }

  return rangeWithDots
})
</script>

<style scoped>
th {
  transition: all 0.2s ease;
}

tr {
  transition: background-color 0.2s ease;
}
</style>