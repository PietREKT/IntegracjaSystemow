<template>
  <Bar :data="chartData" :options="{ responsive: true }" />
</template>

<script setup>
import { computed } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title, Tooltip, Legend,
  BarElement, CategoryScale, LinearScale
} from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const props = defineProps(['data', 'xField', 'yField'])

const chartData = computed(() => ({
  labels: props.data.map(item => item[props.xField]),
  datasets: [
    {
      label: props.yField,
      data: props.data.map(item => item[props.yField]),
      backgroundColor: '#3b82f6',
    }
  ]
}))
</script>
