<template>
  <div>
    <h2 class="text-2xl font-semibold mb-4">Panel wizualizacji</h2>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded shadow p-4">
        <h3 class="font-semibold mb-2">Średnie ceny mieszkań (lata)</h3>
        <LineChart :data="priceOverYears" x-field="year" y-field="price" />
      </div>
      <div class="bg-white rounded shadow p-4">
        <h3 class="font-semibold mb-2">Stopy procentowe (lata)</h3>
        <LineChart :data="ratesOverYears" x-field="year" y-field="rate" />
      </div>
      <div class="bg-white rounded shadow p-4">
        <h3 class="font-semibold mb-2">Ceny mieszkań w miastach</h3>
        <BarChart :data="cityPrices" x-field="city" y-field="price" />
      </div>
      <div class="bg-white rounded shadow p-4">
        <h3 class="font-semibold mb-2">Wzrost cen rok do roku</h3>
        <BarChart :data="priceGrowth" x-field="year" y-field="growth" />
      </div>
    </div>
  </div>
</template>

<script setup>
import LineChart from '../components/LineChart.vue'
import BarChart from '../components/BarChart.vue'


const priceOverYears = [
  { year: '2018', price: 7000 },
  { year: '2019', price: 7400 },
  { year: '2020', price: 7800 },
  { year: '2021', price: 8300 },
  { year: '2022', price: 8900 },
  { year: '2023', price: 9500 },
]

const ratesOverYears = [
  { year: '2018', rate: 1.5 },
  { year: '2019', rate: 1.5 },
  { year: '2020', rate: 0.1 },
  { year: '2021', rate: 0.25 },
  { year: '2022', rate: 6.75 },
  { year: '2023', rate: 5.75 },
]

const cityPrices = [
  { city: 'Warszawa', price: 11000 },
  { city: 'Kraków', price: 10000 },
  { city: 'Gdańsk', price: 9500 },
  { city: 'Wrocław', price: 9200 },
  { city: 'Poznań', price: 8800 },
]

const priceGrowth = [
  { year: '2019', growth: 6 },
  { year: '2020', growth: 5 },
  { year: '2021', growth: 7 },
  { year: '2022', growth: 8 },
  { year: '2023', growth: 6 },
]
</script>