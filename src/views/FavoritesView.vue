<template>
    <section class="favorites">
      <header class="favorites-header">
        <h1>Favorittsøk</h1>
        <p>Her ser du reisene du har lagret som favoritt.</p>
      </header>
  
      <!-- Laster -->
      <div v-if="loading" class="state">Laster favoritter…</div>
  
      <!-- Feil -->
      <div v-else-if="error" class="state state-error">
        ⚠️ {{ error }}
      </div>
  
      <!-- Ingen favoritter -->
      <div v-else-if="!favorites.length" class="state">
        Du har ingen favorittsøk ennå.
      </div>
  
      <!-- Liste -->
      <FavoriteList
        v-else
        :favorites="favorites"
        @use="handleUseFavorite"
        @delete="handleDeleteFavorite"
      />
    </section>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { api } from '../api/backend.js'
  import FavoriteList from '../components/FavoriteList.vue'
  
  const router = useRouter()
  
  const favorites = ref([])
  const loading = ref(false)
  const error = ref('')
  
  onMounted(async () => {
    loading.value = true
    error.value = ''
    try {
      const res = await api.getFavorites()
      // Antatt respons: liste eller { favorites: [...] }
      favorites.value = Array.isArray(res) ? res : (res.favorites ?? [])
    } catch (e) {
      console.error(e)
      error.value = 'Kunne ikke hente favoritter.'
    } finally {
      loading.value = false
    }
  })
  
  const handleUseFavorite = (fav) => {
    router.push({
      name: 'search', // eller 'home' hvis HomeView er søkesiden
      query: {
        from: fav.from,
        to: fav.to,
      },
    })
  }
  
  const handleDeleteFavorite = async (id) => {
    try {
      await api.deleteFavorite(id)
      favorites.value = favorites.value.filter((f) => f.id !== id)
    } catch (e) {
      console.error(e)
      error.value = 'Kunne ikke slette favoritt.'
    }
  }
  </script>
  
  <style scoped>
  .favorites {
    max-width: 800px;
    margin: 0 auto;
    padding: 40px 20px 80px;
  }
  
  .favorites-header {
    text-align: center;
    margin-bottom: 24px;
  }
  
  .favorites-header h1 {
    font-size: 2rem;
    color: var(--color-primary);
    margin-bottom: 8px;
  }
  
  .favorites-header p {
    color: #555;
  }
  
  .state {
    text-align: center;
    color: #666;
    margin-top: 20px;
  }
  
  .state-error {
    color: #b00020;
  }
  </style>