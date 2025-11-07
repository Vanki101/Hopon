<template>
  <section class="home">
    <header class="home-header">
      <h1 class="home-title">HopOn Transport</h1>
      <p class="home-subtitle">Planlegg reisen din enkelt og raskt</p>
    </header>

    <main class="home-main">
      <SearchBar
        v-model="searchQuery"
        placeholder="Søk etter holdeplass..."
        :suggestions="suggestions"
        :isLoading="isSearching"
        @search="handleSearch"
        @clear="handleClear"
      />

      <div v-if="searchQuery" class="search-debug">
        <h3>Søkeresultater for "{{ searchQuery }}"</h3>
        <TripList :trips="tripMock" />
      </div>

      <div v-else class="home-hint">
        <p> Start med å søke etter et sted eller holdeplass for å finne reiser.</p>
      </div>
    </main>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import SearchBar from '../components/SearchBar.vue'
import TripList from '../components/TripList.vue'

// State
const searchQuery = ref('')
const isSearching = ref(false)
const suggestions = ref([])

// Midlertidige testdata (mock)
const tripMock = [
  { id: '1', line: '3', from: 'Halden', to: 'Sarpsborg', departureTime: '12:05', arrivalTime: '12:45', duration: 40, price: 58 },
  { id: '2', line: '7', from: 'Halden', to: 'Fredrikstad', departureTime: '13:10', arrivalTime: '13:50', duration: 40, price: 65 },
]

// Simulert søk (API-delay)
const handleSearch = (query) => {
  isSearching.value = true
  setTimeout(() => {
    suggestions.value = query ? tripMock.map(t => `${t.from} → ${t.to}`) : []
    isSearching.value = false
  }, 400)
}

const handleClear = () => {
  searchQuery.value = ''
  suggestions.value = []
}
</script>
