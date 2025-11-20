<template>
  <section class="search-home">
    <!-- HEADER -->
    <header class="home-header">
      <h1 class="app-title">🚌 HopOn Transport</h1>
      <p class="app-subtitle">Planlegg reisen din enkelt, raskt og pålitelig</p>
    </header>

    <main class="search-main">
      <!-- SØK-SKJEMA -->
      <section class="search-form">
        <div class="search-inputs">
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
        </div>

        <!-- SØK-KNAPP -->
        <button
          class="search-btn"
          :disabled="!from || !to || isSearching"
          @click="handleTripSearch"
        >
          <span v-if="!isSearching">🔎 Søk reise</span>
          <span v-else>
            <span class="btn-spinner"></span>
            Søker…
          </span>
        </button>

        <!-- MELDINGER -->
        <Transition name="fade">
          <div v-if="error" class="message error-message">
            <span class="message-icon">⚠️</span>
            {{ error }}
          </div>
        </Transition>

        <Transition name="fade">
          <div v-if="lastSavedMessage" class="message success-message">
            <span class="message-icon">✓</span>
            {{ lastSavedMessage }}
          </div>
        </Transition>
      </section>

      <!-- RESULTATER -->
      <Transition name="slide-up">
        <section v-if="trips.length" class="results">
          <div class="results-header">
            <h2>
              <span class="results-icon">🚌</span>
              Reiseforslag
            </h2>

            <button class="fav-btn" @click="handleSaveFavorite">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
                <path d="M11.645 20.91l-.007-.003-.022-.012a15.247 15.247 0 01-.383-.218 25.18 25.18 0 01-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0112 5.052 5.5 5.5 0 0116.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 01-4.244 3.17 15.247 15.247 0 01-.383.219l-.022.012-.007.004-.003.001a.752.752 0 01-.704 0l-.003-.001z" />
              </svg>
              Lagre favoritt
            </button>
          </div>

          <TripList
            :trips="trips"
            @select="handleSelectTrip"
          />
        </section>
      </Transition>

      <!-- INGEN REISER -->
      <Transition name="fade">
        <section v-if="!isSearching && from && to && trips.length === 0" class="empty-state">
          <div class="empty-content">
            <span class="empty-icon">😕</span>
            <p>Fant ingen reiser mellom <strong>{{ from }}</strong> og <strong>{{ to }}</strong>.</p>
            <p class="empty-hint">Prøv å søke på ulike stedsnavn eller stasjoner.</p>
          </div>
        </section>
      </Transition>

      <!-- VEILEDNING -->
      <Transition name="fade">
        <section v-if="!from && !to && !isSearching" class="info-state">
          <div class="info-content">
            <span class="info-icon">💡</span>
            <p>Start med å skrive inn avreisested og destinasjon.</p>
          </div>
        </section>
      </Transition>
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

// HENT SØKEFORSLAG – FRA
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

// HENT SØKEFORSLAG – TIL
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
  if (!from.value || !to.value) return

  isSearching.value = true
  error.value = ""
  lastSavedMessage.value = ""
  trips.value = []

  try {
    const res = await api.planTrip(from.value, to.value)
    const patterns = res?.data?.trip?.tripPatterns ?? []

    trips.value = patterns.map((p, index) => {
      const firstLeg = p.legs[0]
      const lastLeg = p.legs[p.legs.length - 1]

      // Samle alle legs (etapper) med stopp-informasjon
      const legs = p.legs.map(leg => ({
        mode: leg.mode,
        line: leg.line?.publicCode || null,
        fromPlace: leg.fromPlace.name,
        toPlace: leg.toPlace.name,
        fromQuay: leg.fromPlace.quay?.publicCode || null,
        toQuay: leg.toPlace.quay?.publicCode || null,
        departureTime: new Date(leg.expectedStartTime).toLocaleTimeString('no-NO', { 
          hour: '2-digit', 
          minute: '2-digit' 
        }),
        arrivalTime: new Date(leg.expectedEndTime).toLocaleTimeString('no-NO', { 
          hour: '2-digit', 
          minute: '2-digit' 
        }),
        duration: Math.round(leg.duration / 60),
        distance: leg.distance
      }))

      return {
        id: index,
        from: firstLeg.fromPlace.name,
        to: lastLeg.toPlace.name,
        line: firstLeg.line?.publicCode || 'N/A',
        departureTime: new Date(firstLeg.expectedStartTime).toLocaleTimeString('no-NO', { 
          hour: '2-digit', 
          minute: '2-digit' 
        }),
        arrivalTime: new Date(lastLeg.expectedEndTime).toLocaleTimeString('no-NO', { 
          hour: '2-digit', 
          minute: '2-digit' 
        }),
        duration: Math.round(p.duration / 60),
        price: null,
        legs: legs, // ⭐ Alle etapper med stopp
        walkDistance: Math.round(p.walkDistance || 0)
      }
    })

    if (trips.value.length === 0) {
      error.value = "Ingen reiser funnet for denne ruten."
    }

  } catch (e) {
    console.error(e)
    error.value = "Kunne ikke hente reiser. Prøv igjen senere."
  } finally {
    isSearching.value = false
  }
}

// LAGRE FAVORITT
const handleSaveFavorite = async () => {
  if (!from.value || !to.value) return

  try {
    await api.saveFavorite(from.value, to.value)
    lastSavedMessage.value = `Favoritt lagret: ${from.value} → ${to.value}`
    
    // Fjern melding etter 3 sekunder
    setTimeout(() => {
      lastSavedMessage.value = ''
    }, 3000)
  } catch (e) {
    console.error(e)
    error.value = 'Kunne ikke lagre favoritt'
  }
}

// NAVIGER TIL TRIP-DETAILS
const handleSelectTrip = (trip) => {
  // Lagre trip data i localStorage
  localStorage.setItem('selectedTrip', JSON.stringify(trip))
  
  // Naviger til trip view
  router.push({
    name: 'trip',
    query: { id: trip.id }
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
.home-header {
  text-align: center;
  margin-bottom: 40px;
}

.app-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #cf7012;
  margin-bottom: 12px;
}

.app-subtitle {
  color: #666;
  font-size: 1.1rem;
}

/* SEARCH FORM */
.search-main {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.search-form {
  background: #fff;
  padding: 28px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.search-inputs {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-btn {
  background: linear-gradient(135deg, #cf7012 0%, #b85f15 100%);
  color: #fff;
  border: none;
  padding: 14px 20px;
  font-size: 1.05rem;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s ease;
  box-shadow: 0 4px 10px rgba(207, 112, 18, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.search-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #b85f15 0%, #a05413 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 14px rgba(207, 112, 18, 0.3);
}

.search-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

/* MESSAGES */
.message {
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 10px;
}

.error-message {
  background: #fee;
  color: #c33;
  border-left: 4px solid #c33;
}

.success-message {
  background: #eff;
  color: #2a7;
  border-left: 4px solid #2a7;
}

.message-icon {
  font-size: 1.2rem;
}

/* RESULTS */
.results {
  background: #fff;
  padding: 28px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.results-header h2 {
  font-size: 1.5rem;
  color: #222;
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0;
}

.results-icon {
  font-size: 1.8rem;
}

.fav-btn {
  background: #fff;
  padding: 10px 16px;
  border-radius: 10px;
  border: 2px solid #cf7012;
  color: #cf7012;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s ease;
}

.fav-btn:hover {
  background: #cf7012;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(207, 112, 18, 0.2);
}

/* EMPTY STATE */
.empty-state,
.info-state {
  background: #fff;
  padding: 40px 28px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  text-align: center;
}

.empty-content,
.info-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon,
.info-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 16px;
}

.empty-state p,
.info-state p {
  color: #555;
  font-size: 1rem;
  margin: 8px 0;
}

.empty-hint {
  color: #999 !important;
  font-size: 0.9rem !important;
}

/* ANIMATIONS */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.slide-up-enter-active {
  transition: all 0.4s ease;
}

.slide-up-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

/* RESPONSIVE */
@media (max-width: 600px) {
  .app-title {
    font-size: 2rem;
  }

  .search-form {
    padding: 20px;
  }

  .results-header {
    flex-direction: column;
    align-items: stretch;
  }

  .fav-btn {
    justify-content: center;
  }
}
</style>