<template>
  <section class="home">
    <!-- HERO -->
    <header class="home-header">
      <div class="hero-card">
        <h1 class="hero-title">HopOn Transport 🚍</h1>
        <p class="hero-subtitle">Planlegg reisen din enkelt, raskt og pålitelig</p>
      </div>
    </header>

    <!-- MAIN -->
    <main class="home-main">
      <!-- SØKEBOKS -->
      <div class="search-card">
        <SearchBar
          v-model="searchQuery"
          placeholder="Søk etter holdeplass..."
          :suggestions="suggestions"
          :isLoading="isSearching"
          @search="handleSearch"
          @clear="handleClear"
        />
      </div>

      <!-- RESULTATER -->
      <div v-if="searchQuery && trips.length" class="results-card">
        <h3>Reiseforslag</h3>
        <TripList :trips="trips" @select="handleSelectTrip" />
      </div>

      <!-- KART -->
      <div id="map" class="map-container"></div>

      <!-- HINT -->
      <div v-if="!searchQuery && !isSearching" class="home-hint">
        <p>💡 Start med å søke etter et sted eller holdeplass for å finne reiser.</p>
      </div>
    </main>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import SearchBar from '../components/SearchBar.vue'
import TripList from '../components/TripList.vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

const router = useRouter()
const searchQuery = ref('')
const isSearching = ref(false)
const suggestions = ref([])
const trips = ref([])

const tripMock = [
  { id: '1', line: '3', from: 'Halden', to: 'Sarpsborg', departureTime: '12:05', arrivalTime: '12:45', duration: 40, price: 58, coords: [59.125, 11.387] },
  { id: '2', line: '7', from: 'Halden', to: 'Fredrikstad', departureTime: '13:10', arrivalTime: '13:50', duration: 40, price: 65, coords: [59.213, 10.939] },
]

const handleSearch = (query) => {
  isSearching.value = true
  setTimeout(() => {
    suggestions.value = query ? tripMock.map(t => `${t.from} → ${t.to}`) : []
    trips.value = tripMock.filter(t =>
      t.from.toLowerCase().includes(query.toLowerCase()) ||
      t.to.toLowerCase().includes(query.toLowerCase())
    )
    isSearching.value = false
  }, 400)
}

const handleClear = () => {
  searchQuery.value = ''
  suggestions.value = []
  trips.value = []
}

const handleSelectTrip = (trip) => {
  router.push({
    name: 'trip',
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

// KART
onMounted(() => {
  const map = L.map('map').setView([59.12, 11.38], 8)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap',
  }).addTo(map)
  tripMock.forEach((trip) => {
    L.marker(trip.coords).addTo(map).bindPopup(`<b>${trip.from}</b> → ${trip.to}`)
  })
})
</script>

<style scoped>
/* --- LAYOUT --- */
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 60px;
  background: #f8fafc;
}

.home-header {
  text-align: center;
  margin-top: 5px;
  margin-bottom: 20px;
}

/* --- HERO --- */
.hero-card {
  background: linear-gradient(120deg, #2ea3a3, #cf7012);
  color: rgb(238, 238, 238);
  padding: 50px 80px;
  border-radius: 22px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(12px);
  display: inline-block;
  transition: transform 0.3s ease;
}

.hero-card:hover {
  transform: translateY(-3px);
}

.hero-title {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 10px;
}

.hero-subtitle {
  font-size: 1.2rem;
  opacity: 0.95;
}

/* --- MAIN --- */
.home-main {
  width: 100%;
  max-width: 1000px;
  padding: 0 20px;
}

/* --- SØKEBOKS --- */
.search-card {
  background: white;
  padding: 20px 24px;
  border-radius: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  margin-bottom: 30px;
}

/* --- RESULTATER --- */
.results-card {
  background: white;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  margin-bottom: 30px;
}

.results-card h3 {
  color: #cf7012;
  margin-bottom: 12px;
}

/* --- KART --- */
#map {
  width: 100%;
  height: 180px;
  margin: 50px 2px;
  border-radius: 14px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* --- HINT --- */
.home-hint {
  text-align: center;
  color: #555;
  margin-top: 15px;
  font-size: 1rem;
}

/* --- RESPONSIV --- */
@media (max-width: 800px) {
  .hero-card {
    padding: 1px 260px;
  }
  .hero-subtitle {
    font-size: 0.5rem;
  }
  #map {
    height: 200px;
  }
}
</style>
