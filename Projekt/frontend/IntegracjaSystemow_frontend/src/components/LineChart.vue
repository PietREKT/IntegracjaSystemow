<template>
  <canvas ref="canvas"></canvas>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { Chart, LineController, LineElement, PointElement, LinearScale, CategoryScale, Title } from 'chart.js'

Chart.register(LineController, LineElement, PointElement, LinearScale, CategoryScale, Title)

const props = defineProps(['data', 'xField', 'yField'])
const canvas = ref(null)
let chartInstance

onMounted(() => {
  chartInstance = new Chart(canvas.value, {
    type: 'line',
    data: {
      labels: props.data.map(d => d[props.xField]),
      datasets: [{
        label: props.yField,
        data: props.data.map(d => d[props.yField]),
        fill: false,
        borderColor: 'rgb(59, 130, 246)',
        tension: 0.1
      }]
    }
  })
})

watch(() => props.data, () => chartInstance?.update(), { deep: true })
</script>
