<template>
  <section class="search-home">
    <!-- HEADER -->
    <header class="home-header">
      <div class="header-card">
        <h1 class="app-title">HopOn Transport</h1>
        <p class="app-subtitle">Planlegg reisen din enkelt, raskt og pålitelig</p>
      </div>
    </header>

    <main class="search-main">

      <!-- 🔍 SØK-SKJEMA -->
      <section class="search-form">

        <!-- FRA -->
        <SearchBar
          v-model="from"
          placeholder="Fra: f.eks. Halden stasjon"
          :suggestions="fromSuggestions"
          :isLoading="isLoadingFrom"
          @search="handleFromSearch"
          @clear="handleClearFrom"
        />

        <!-- TIL -->
        <SearchBar
          v-model="to"
          placeholder="Til: f.eks. Sarpsborg bussterminal"
          :suggestions="toSuggestions"
          :isLoading="isLoadingTo"
          @search="handleToSearch"
          @clear="handleClearTo"
        />

        <!-- 🔎 SØK-KNAPP -->
        <button
          class="search-btn"
          :disabled="!from || !to || isSearching"
          @click="handleTripSearch"
        >
          {{ isSearching ? 'Søker…' : 'Søk reise' }}
        </button>

        <!-- MELDINGER -->
        <p v-if="error" class="error-text">{{ error }}</p>
        <p v-if="lastSavedMessage" class="info-text">{{ lastSavedMessage }}</p>
      </section>

      <!-- 🚌 RESULTATER -->
      <section v-if="trips.length" class="results">
        <div class="results-header">
          <h2>Reiseforslag</h2>

          <button class="fav-btn" @click="handleSaveFavorite">
            ⭐ Lagre dette søket
          </button>
        </div>

        <TripList
          :trips="trips"
          @select="handleSelectTrip"
        />
      </section>

      <!-- 😕 INGEN REISER -->
      <section v-else-if="!isSearching && from && to" class="empty-state">
        <p>😕 Fant ingen reiser mellom {{ from }} og {{ to }}.</p>
      </section>

      <!-- ℹ️ VEILEDNING -->
      <section v-else-if="!from && !to" class="info-state">
        <p>💡 Start med å skrive inn avreisested og destinasjon.</p>
      </section>

    </main>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import SearchBar from '../components/SearchBar.vue'
import TripList from '../components/TripList.vue'
import { api } from '../api/backend.js'

const router = useRouter()
const route = useRoute()

// SØKEINPUT
const from = ref('')
const to = ref('')
const trips = ref([])

// STATES
const isSearching = ref(false)
const isLoadingFrom = ref(false)
const isLoadingTo = ref(false)
const error = ref('')
const lastSavedMessage = ref('')

// FORSLAG
const fromSuggestions = ref([])
const toSuggestions = ref([])

// Hvis man kommer fra FavoritesView med query params
if (route.query.from) from.value = String(route.query.from)
if (route.query.to) to.value = String(route.query.to)


// =============================
// 🔍 HENT SØKEFORSLAG – FRA
// =============================
const handleFromSearch = async (query) => {
  from.value = query
  if (!query) return fromSuggestions.value = []

  isLoadingFrom.value = true
  try {
    const res = await api.searchStops(query)
    fromSuggestions.value = Array.isArray(res)
      ? res.map(s => s.name ?? s)
      : []
  } catch (e) {
    console.error(e)
  } finally {
    isLoadingFrom.value = false
  }
}

// =============================
// 🔍 HENT SØKEFORSLAG – TIL
// =============================
const handleToSearch = async (query) => {
  to.value = query
  if (!query) return toSuggestions.value = []

  isLoadingTo.value = true
  try {
    const res = await api.searchStops(query)
    toSuggestions.value = Array.isArray(res)
      ? res.map(s => s.name ?? s)
      : []
  } catch (e) {
    console.error(e)
  } finally {
    isLoadingTo.value = false
  }
}

// CLEAR
const handleClearFrom = () => {
  from.value = ''
  fromSuggestions.value = []
}

const handleClearTo = () => {
  to.value = ''
  toSuggestions.value = []
}

const handleTripSearch = async () => {
  if (!from.value || !to.value) return;

  isSearching.value = true;
  error.value = "";
  lastSavedMessage.value = "";
  trips.value = [];

  try {
    const res = await api.planTrip(from.value, to.value);

    // viktig: hent ut tripPatterns fra backend
    const patterns = res?.data?.trip?.tripPatterns ?? [];

    trips.value = patterns.map((p, index) => {
      const firstLeg = p.legs[0];
      const lastLeg = p.legs[p.legs.length - 1];

      return {
        id: index,
        from: firstLeg.fromPlace.name,
        to: lastLeg.toPlace.name,
        departureTime: firstLeg.expectedStartTime,
        arrivalTime: lastLeg.expectedEndTime,
        duration: Math.round(p.duration / 60),
        price: null // entur gir ikke pris
      };
    });

  } catch (e) {
    console.error(e);
    error.value = "Kunne ikke hente reiser. Prøv igjen senere.";
  } finally {
    isSearching.value = false;
  }
};

// =============================
// ⭐ LAGRE FAVORITT
// =============================
const handleSaveFavorite = async () => {
  if (!from.value || !to.value) return

  try {
    await api.saveFavorite(from.value, to.value)
    lastSavedMessage.value = `Favoritt lagret: ${from.value} → ${to.value} ✔️`
  } catch (e) {
    console.error(e)
    lastSavedMessage.value = 'Kunne ikke lagre favoritt 😕'
  }
}

// =============================
// 👉 NAVIGER TIL TRIP-DETAILS
// =============================
const handleSelectTrip = (trip) => {
  router.push({
    name: 'trip',
    query: { ...trip }
  })
}
</script>

<style scoped>
.search-home {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px 80px;
}

/* HEADER */
.search-header {
  text-align: center;
  margin-bottom: 30px;
}

.search-header h1 {
  font-size: 2.3rem;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 8px;
}

.search-header p {
  color: #555;
  font-size: 1rem;
}

/* SØKEFELT */
.search-main {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.search-form {
  background: #fff;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-btn {
  background: #cf7012;
  color: #fff;
  border: none;
  padding: 12px 16px;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s ease;
}

.search-btn:hover:not(:disabled) {
  background: #b55f15;
}

.error-text {
  color: #b00020;
  font-size: 0.9rem;
}
.info-text {
  color: #2ea3a3;
  font-size: 0.9rem;
}

/* RESULTATER */
.results {
  background: #fff;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.fav-btn {
  background: #eee;
  padding: 8px 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
  cursor: pointer;
}

.fav-btn:hover {
  background: #ddd;
}

.empty-state,
.info-state {
  text-align: center;
  color: #666;
}
</style>