<template>
  <canvas ref="canvas"></canvas>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { Chart, BarController, BarElement, CategoryScale, LinearScale, Title } from 'chart.js'

Chart.register(BarController, BarElement, CategoryScale, LinearScale, Title)

const props = defineProps(['data', 'xField', 'yField'])
const canvas = ref(null)
let chartInstance

onMounted(() => {
  chartInstance = new Chart(canvas.value, {
    type: 'bar',
    data: {
      labels: props.data.map(d => d[props.xField]),
      datasets: [{
        label: props.yField,
        data: props.data.map(d => d[props.yField]),
        backgroundColor: 'rgb(59, 130, 246)'
      }]
    }
  })
})

watch(() => props.data, () => chartInstance?.update(), { deep: true })
</script>
