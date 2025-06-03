<template>
  <Line :data="chartData" :options="{ responsive: true }" />
</template>

<script setup>
import { computed } from 'vue'
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title, Tooltip, Legend,
  LineElement, CategoryScale, LinearScale, PointElement
} from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement)

const props = defineProps(['data', 'xField', 'yField'])

const chartData = computed(() => ({
  labels: props.data.map(item => item[props.xField]),
  datasets: [
    {
      label: props.yField,
      data: props.data.map(item => item[props.yField]),
      borderColor: 'blue',
      backgroundColor: 'lightblue',
      tension: 0.2,
      fill: false,
    }
  ]
}))
</script>

