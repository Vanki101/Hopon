<template>
  <div id="app">
    <header class="app-header">
      <h1>SearchBar </h1>
      <p>Testing Issue #35 - SearchBar komponent</p>
    </header>

    <main class="app-main">
      <div class="test-section">
        <h2> SearchBar</h2>
        
        <SearchBar
          v-model="searchQuery"
          placeholder="Søk etter holdeplass..."
          :suggestions="suggestions"
          :isLoading="isSearching"
          @search="handleSearch"
          @clear="handleClear"
        />

        <!-- Debug info -->
        <div v-if="searchQuery" class="debug-box">
          <h3>Debug Info</h3>
          <p><strong>Søkeord:</strong> {{ searchQuery }}</p>
          <p><strong>Antall forslag:</strong> {{ suggestions.length }}</p>
        </div>

        <div v-else class="hint-box">
          <p>💡 Skriv noe i søkefeltet for å se forslag</p>
        </div>
      </div>

    
      <section style="margin-top:24px; padding-top:12px; border-top:1px solid #eee;">
        <h2 style="font-size:20px; font-weight:600; margin:0 0 12px;">TripList – demo</h2>
        <TripList :trips="tripMock" @select="t => console.log('Valgt reise:', t)" />
      </section>

    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SearchBar from './components/SearchBar.vue'
import TripList from './components/TripList.vue'

// State
const searchQuery = ref('')
const isSearching = ref(false)

// Mock suggestions - simulerer Entur API
const allSuggestions = [
  'Oslo S',
  'Nationaltheatret',
  'Majorstuen',
  'Stortinget',
  'Jernbanetorget',
  'Grønland',
  'Tøyen',
  'Carl Berners plass',
  'Sinsen',
  'Storo'
]

const suggestions = ref([])

const tripMock = [                                        // ➕
  { id:'t1', line:'3', from:'Halden', to:'Sarpsborg',  departureTime:'12:05', arrivalTime:'12:45', duration:40 },
  { id:'t2', line:'7', from:'Halden', to:'Fredrikstad', departureTime:'13:10', arrivalTime:'13:50', duration:40 },
]


// Methods
const handleSearch = (query) => {
  console.log(' Søker etter:', query)
  
  isSearching.value = true
  
  // Simuler API-kall med delay
  setTimeout(() => {
    if (query) {
      // Filter suggestions basert på søk
      suggestions.value = allSuggestions.filter(s => 
        s.toLowerCase().includes(query.toLowerCase())
      )
      console.log(' Fant', suggestions.value.length, 'forslag')
    } else {
      suggestions.value = []
    }
    
    isSearching.value = false
  }, 300)
}

const handleClear = () => {
  console.log(' Søket ble tømt')
  searchQuery.value = ''
  suggestions.value = []
}
</script>

<style scoped>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: linear-gradient(135deg, var(--color-primary) 0%, #ee2222 100%);
  color: white;
  padding: var(--spacing-xl);
  text-align: center;
  box-shadow: var(--shadow-lg);
}

.app-header h1 {
  font-size: var(--font-size-2xl);
  margin-bottom: var(--spacing-sm);
}

.app-header p {
  font-size: var(--font-size-base);
  opacity: 0.9;
}

.app-main {
  flex: 1;
  max-width: 800px;
  width: 100%;
  margin: 0 auto;
  padding: var(--spacing-xl);
}

.test-section {
  margin-bottom: var(--spacing-xl);
}

.test-section h2 {
  font-size: var(--font-size-xl);
  font-weight: 600;
  margin-bottom: var(--spacing-lg);
  color: var(--color-gray-900);
}

.debug-box {
  margin-top: var(--spacing-lg);
  padding: var(--spacing-lg);
  background-color: var(--color-gray-100);
  border-radius: var(--border-radius);
  border-left: 4px solid var(--color-primary);
}

.debug-box h3 {
  font-size: var(--font-size-lg);
  margin-bottom: var(--spacing-sm);
  color: var(--color-gray-900);
}

.debug-box p {
  font-size: var(--font-size-base);
  color: var(--color-secondary);
  margin: var(--spacing-xs) 0;
}

.hint-box {
  margin-top: var(--spacing-lg);
  padding: var(--spacing-lg);
  background-color: #fef3c7;
  border-radius: var(--border-radius);
  border-left: 4px solid var(--color-accent);
  text-align: center;
}

.hint-box p {
  color: #000000;
  font-size: var(--font-size-base);
}

/* media qurraies */
@media (max-width: 768px) {
  .app-main {
    padding: var(--spacing-md);
  }

  .app-header h1 {
    font-size: var(--font-size-xl);
  }
}
</style>