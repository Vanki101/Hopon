<template>
  <div class="favorite-list-container">
    <!-- Header -->
    <div class="favorite-header">
      <h2 class="favorite-title">
        <span class="favorite-icon"></span>
        {{ title }}
      </h2>
      <span v-if="favorites.length > 0" class="favorite-count">
        {{ favorites.length }}
      </span>
    </div>

    <!-- Loading state -->
    <div v-if="isLoading" class="loading-container">
      <div class="spinner"></div>
      <p>Laster favoritter...</p>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="error-message">
      <strong> Kunne ikke laste favoritter</strong>
      <p>{{ error }}</p>
    </div>

    <!-- Empty state -->
    <div v-else-if="!favorites || favorites.length === 0" class="empty-state">
      <div class="empty-icon">⭐</div>
      <h3>Ingen favoritter ennå</h3>
      <p>Legg til dine mest brukte holdeplasser for rask tilgang</p>
    </div>

    <!-- Favorites list -->
    <ul v-else class="favorite-list" role="list">
      <li
        v-for="favorite in favorites"
        :key="favorite.id"
        class="favorite-item component-card"
        role="listitem"
      >
        <div class="favorite-content">
          <!-- Icon -->
          <div class="favorite-marker">
            <span class="marker-icon"></span>
          </div>

          <!-- Details -->
          <div class="favorite-details">
            <h3 class="favorite-name">{{ favorite.name }}</h3>
            <p v-if="favorite.lastUsed" class="favorite-last-used">
              Sist brukt: {{ formatDate(favorite.lastUsed) }}
            </p>
          </div>

          <!-- Actions -->
          <div class="favorite-actions">
            <Button
              variant="primary"
              icon-only
              size="small"
              :aria-label="`Søk fra ${favorite.name}`"
              @click="searchFavorite(favorite)"
            >
              <template #icon>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
                </svg>
              </template>
            </Button>

            <Button
              variant="danger"
              icon-only
              size="small"
              :aria-label="`Slett ${favorite.name}`"
              @click="confirmDelete(favorite)"
            >
              <template #icon>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                </svg>
              </template>
            </Button>
          </div>
        </div>
      </li>
    </ul>

    <!-- Delete confirmation modal -->
    <div v-if="showDeleteModal" class="modal-overlay" @click="cancelDelete">
      <div class="modal-content component-card" @click.stop>
        <h3>Fjern favoritt?</h3>
        <p>Er du sikker på at du vil fjerne <strong>{{ favoriteToDelete?.name }}</strong> fra favoritter?</p>
        
        <div class="modal-actions">
          <Button variant="secondary" @click="cancelDelete">
            Avbryt
          </Button>
          <Button variant="danger" @click="deleteFavorite">
            Ja, fjern
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Button from './ui/Button.vue'

// Props
const props = defineProps({
  favorites: {
    type: Array,
    default: () => []
  },
  title: {
    type: String,
    default: 'Mine favoritter'
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  error: {
    type: String,
    default: null
  }
})

// Emits
const emit = defineEmits(['search', 'delete'])

// State
const showDeleteModal = ref(false)
const favoriteToDelete = ref(null)

// Methods
const searchFavorite = (favorite) => {
  emit('search', favorite)
}

const confirmDelete = (favorite) => {
  favoriteToDelete.value = favorite
  showDeleteModal.value = true
}

const deleteFavorite = () => {
  if (favoriteToDelete.value) {
    emit('delete', favoriteToDelete.value)
  }
  cancelDelete()
}

const cancelDelete = () => {
  showDeleteModal.value = false
  favoriteToDelete.value = null
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffInHours = Math.floor((now - date) / (1000 * 60 * 60))
  
  if (diffInHours < 1) return 'Akkurat nå'
  if (diffInHours < 24) return `${diffInHours} timer siden`
  if (diffInHours < 48) return 'I går'
  
  const diffInDays = Math.floor(diffInHours / 24)
  if (diffInDays < 7) return `${diffInDays} dager siden`
  
  return date.toLocaleDateString('nb-NO', { day: 'numeric', month: 'short' })
}
</script>

<style scoped>
.favorite-list-container {
  width: 100%;
}

.favorite-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.favorite-title {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--color-gray-900);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin: 0;
}

.favorite-icon {
  font-size: var(--font-size-2xl);
}

.favorite-count {
  background-color: var(--color-primary);
  color: white;
  padding: var(--spacing-xs) var(--spacing-md);
  border-radius: 20px;
  font-size: var(--font-size-sm);
  font-weight: 600;
}

.loading-container {
  text-align: center;
  padding: var(--spacing-xl);
  color: var(--color-secondary);
}

.spinner {
  border: 3px solid var(--color-gray-200);
  border-top: 3px solid var(--color-primary);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto var(--spacing-md);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: var(--spacing-xl);
  background: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-md);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: var(--spacing-md);
  opacity: 0.3;
}

.empty-state h3 {
  font-size: var(--font-size-lg);
  margin-bottom: var(--spacing-sm);
  color: var(--color-gray-900);
}

.empty-state p {
  color: var(--color-secondary);
  font-size: var(--font-size-sm);
}

.favorite-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.favorite-item {
  padding: var(--spacing-lg);
  transition: all 0.2s ease;
}

.favorite-item:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

.favorite-content {
  display: flex;
  gap: var(--spacing-md);
  align-items: center;
}

.favorite-marker {
  flex-shrink: 0;
  font-size: var(--font-size-2xl);
}

.favorite-details {
  flex: 1;
}

.favorite-name {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--color-gray-900);
  margin: 0 0 var(--spacing-xs) 0;
}

.favorite-last-used {
  font-size: 0.75rem;
  color: var(--color-gray-300);
  margin: 0;
}

.favorite-actions {
  display: flex;
  gap: var(--spacing-sm);
  flex-shrink: 0;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: var(--spacing-lg);
}

.modal-content {
  max-width: 400px;
  width: 100%;
  padding: var(--spacing-xl);
}

.modal-content h3 {
  font-size: var(--font-size-xl);
  margin-bottom: var(--spacing-md);
  color: var(--color-gray-900);
}

.modal-content p {
  margin-bottom: var(--spacing-lg);
  color: var(--color-secondary);
}

.modal-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: flex-end;
}

/* Responsive */
@media (max-width: 768px) {
  .favorite-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }

  .favorite-content {
    flex-wrap: wrap;
  }

  .favorite-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .modal-actions button {
    width: 100%;
  }
}
</style>