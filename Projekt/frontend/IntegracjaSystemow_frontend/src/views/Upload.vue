<template>
  <div>
    <h2 class="text-2xl font-semibold mb-4">Dodaj nowe dane</h2>
    <form class="bg-white p-6 rounded shadow w-full max-w-xl" @submit.prevent="submitForm">
      <label class="block mb-4">
        Wybierz plik CSV:
        <input type="file" accept=".csv" @change="handleFile" class="mt-2 border p-2 w-full" />
      </label>
      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Wyślij</button>
    </form>

    <div v-if="preview.length" class="mt-6">
      <h3 class="font-semibold mb-2">Podgląd danych:</h3>
      <pre class="bg-gray-100 p-4 rounded text-sm max-h-64 overflow-auto">{{ preview.join('\n') }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const file = ref(null)
const preview = ref([])

function handleFile(event) {
  const selected = event.target.files[0]
  file.value = selected

  if (selected) {
    const reader = new FileReader()
    reader.onload = () => {
      preview.value = reader.result.split('\n').slice(0, 10)
    }
    reader.readAsText(selected)
  }
}

function submitForm() {
  if (!file.value) return alert('Wybierz plik przed wysłaniem')
  alert('Plik gotowy do wysłania! (symulacja)')
}
</script>
