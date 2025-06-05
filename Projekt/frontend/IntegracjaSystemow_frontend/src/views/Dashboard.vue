<template>
  <div>
    <h2 class="text-2xl font-semibold mb-4">Panel wizualizacji</h2>

    <!-- Wybór zakresu lat -->
    <div class="mb-6 flex gap-4 items-center">
      <label class="font-medium">Zakres danych:</label>
      <label class="flex gap-2 items-center">
        <input type="radio" name="range" value="2010" v-model="minYear" />
        od 2010
      </label>
      <label class="flex gap-2 items-center">
        <input type="radio" name="range" value="2018" v-model="minYear" />
        od 2018
      </label>
      <label class="flex gap-2 items-center">
        <input type="radio" name="range" value="2020" v-model="minYear" />
        od 2020
      </label>
    </div>

    <!-- Wykresy -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white p-4 rounded shadow">
        <h3 class="font-semibold mb-2">Średnie ceny mieszkań (lata)</h3>
        <LineChart :data="avgPricesByYear" x-field="year" y-field="price" y-desc="Cena"/>
      </div>
      <div class="bg-white p-4 rounded shadow">
        <h3 class="font-semibold mb-2">Średnia cena za m² w miastach (ostatni rok)</h3>
        <BarChart :data="avgPriceByCity" x-field="city" y-field="price" y-desc="Cena"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import LineChart from '../components/LineChart.vue'
import BarChart from '../components/BarChart.vue'
import {
  getAvgPricesByYear,
  getTransactionsPerYear,
  getAvgPriceByCity,
} from '../api/houses'

const avgPricesByYear = ref([])
const avgRatesByYear = ref([])
const avgPriceByCity = ref([])
const priceGrowthYearly = ref([])
const minYear = ref('2010')

watch(minYear, reloadData)

async function reloadData() {
  try {
    const prices = await getAvgPricesByYear()
    avgPricesByYear.value = prices;
    avgPricesByYear.value = Object.entries(prices)
        .filter(([year]) => Number(year) >= Number(minYear.value))
        .map(([year, price]) => ({
          year: String(year),
          price: Number(price)
        }))

    const counts = await getTransactionsPerYear()
    const sortedCounts = Object.entries(counts)
        .filter(([year]) => Number(year) >= Number(minYear.value))
        .map(([year, count]) => ({ year: String(year), value: Number(count) }))
        .sort((a, b) => a.year - b.year)

    priceGrowthYearly.value = sortedCounts.map((curr, i, arr) => {
      if (i === 0) return { year: curr.year, growth: 0 }
      const prev = arr[i - 1]
      const growth = ((curr.value - prev.value) / prev.value) * 100
      return { year: curr.year, growth: Math.round(Number(growth)) }
    })

    const cities = await getAvgPriceByCity()
    avgPriceByCity.value = Object.entries(cities)
        .map(([city, price]) => ({
          city,
          price: Number(price)
        }))
        .sort((a, b) => b.price - a.price)

  } catch (error) {
    console.error('Błąd przy ładowaniu danych:', error)
  }
}

onMounted(reloadData)
</script>

