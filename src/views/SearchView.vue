<template>
  <div class="search-view">
    <section class="search-header">
      <h1>Finn din reise</h1>
      <p>Planlegg reisen raskt og enkelt med HopOn.</p>
    </section>

    <section class="search-form">
      <!-- FRA-input -->
      <SearchBar
        v-model="from"
        placeholder="Fra: f.eks. Halden stasjon"
        :suggestions="fromSuggestions"
        :isLoading="isLoadingFrom"
        @search="handleFromSearch"
        @clear="() => (from = '')"
      />

      <!-- TIL-input -->
      <SearchBar
        v-model="to"
        placeholder="Til: f.eks. Sarpsborg bussterminal"
        :suggestions="toSuggestions"
        :isLoading="isLoadingTo"
        @search="handleToSearch"
        @clear="() => (to = '')"
      />

      <button
        class="search-btn"
        :disabled="!from || !to || isSearching"
        @click="handleTripSearch"
      >
        {{ isSearching ? 'Søker...' : 'Søk reise' }}
      </button>
    </section>

    <!-- Resultater -->
    <section v-if="trips.length" class="results">
      <h2>Reiseforslag</h2>
      <TripList :trips="trips" @select="handleSelectTrip" />
    </section>

    <!-- Ingen treff -->
    <section v-else-if="!isSearching && from && to" class="empty-state">
      <p>😕 Fant ingen reiser mellom {{ from }} og {{ to }}.</p>
    </section>

    <!-- Før søk -->
    <section v-else-if="!from && !to" class="info-state">
      <p>💡 Skriv inn avreisested og destinasjon for å starte.</p>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import SearchBar from '../components/SearchBar.vue'
import TripList from '../components/TripList.vue'

const router = useRouter()

// STATE
const from = ref('')
const to = ref('')
const trips = ref([])

const isSearching = ref(false)
const isLoadingFrom = ref(false)
const isLoadingTo = ref(false)

const fromSuggestions = ref([])
const toSuggestions = ref([])

// MOCK DATA
const mockTrips = [
  { id: 1, line: '3', from: 'Halden', to: 'Sarpsborg', departureTime: '12:05', arrivalTime: '12:45', duration: 40, price: 58 },
  { id: 2, line: '7', from: 'Halden', to: 'Fredrikstad', departureTime: '13:10', arrivalTime: '13:50', duration: 40, price: 65 },
]

// FUNKSJONER
const handleFromSearch = (query) => {
  isLoadingFrom.value = true
  setTimeout(() => {
    fromSuggestions.value = query
      ? mockTrips.map(t => t.from).filter((v, i, a) => a.indexOf(v) === i)
      : []
    isLoadingFrom.value = false
  }, 400)
}

const handleToSearch = (query) => {
  isLoadingTo.value = true
  setTimeout(() => {
    toSuggestions.value = query
      ? mockTrips.map(t => t.to).filter((v, i, a) => a.indexOf(v) === i)
      : []
    isLoadingTo.value = false
  }, 400)
}

const handleTripSearch = async () => {
  isSearching.value = true
  trips.value = []

  // Simulerer søk
  setTimeout(() => {
    trips.value = mockTrips.filter(
      (t) =>
        t.from.toLowerCase().includes(from.value.toLowerCase()) &&
        t.to.toLowerCase().includes(to.value.toLowerCase())
    )
    isSearching.value = false
  }, 700)
}

// ✅ Denne må ligge utenfor handleTripSearch
const handleSelectTrip = (trip) => {
  router.push({
    name: 'trip', // må matche router/index.ts
    query: {
      id: trip.id,
      from: trip.from,
      to: trip.to,
      line: trip.line,
      departureTime: trip.departureTime,
      arrivalTime: trip.arrivalTime,
      duration: trip.duration,
      price: trip.price,
    },
  })
}
</script>

<style scoped>
.search-view {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px 80px;
}

.search-header {
  text-align: center;
  margin-bottom: 40px;
}

.search-header h1 {
  color: var(--color-primary);
  font-size: 2rem;
  margin-bottom: 10px;
}

.search-header p {
  color: #555;
}

/* FORM */
.search-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin-bottom: 40px;
}

.search-btn {
  background: #cf7012;
  color: #fff;
  border: none;
  padding: 14px;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.search-btn:hover {
  background: #b55f15;
}

.search-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* RESULTS */
.results {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.results h2 {
  margin-bottom: 16px;
  color: var(--color-primary);
}

/* STATES */
.empty-state, .info-state {
  text-align: center;
  color: #666;
  font-size: 1rem;
  margin-top: 40px;
}
</style>
