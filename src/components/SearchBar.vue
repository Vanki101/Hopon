<template>
  <div class="search-bar-container">
    <div 
      class="search-bar component-card"
      @keydown="handleKeydown"
    >
      <!-- Ikon -->
      <div class="search-icon">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="20" height="20">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
        </svg>
      </div>
      
      <!-- Input -->
      <input
        ref="searchInput"
        type="text"
        class="input search-input"
        :placeholder="placeholder"
        :value="modelValue"
        @input="handleInput"
        @focus="isFocused = true"
        @blur="handleBlur"
        :aria-label="placeholder || 'Søk etter reiser'"
        :disabled="isLoading"
      />

      <!-- Clear knapp -->
      <button
        v-if="modelValue"
        @click="clearSearch"
        class="clear-button"
        aria-label="Tøm søkefelt"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="20" height="20">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>

      <!-- Loader -->
      <div v-if="isLoading" class="loading-indicator">
        <div class="mini-spinner"></div>
      </div>
    </div>

    <!-- Forslag -->
    <div 
      v-if="showSuggestions && suggestions.length > 0" 
      class="suggestions-dropdown component-card"
    >
      <ul role="listbox">
        <li
          v-for="(suggestion, index) in suggestions"
          :key="index"
          @click="selectSuggestion(suggestion)"
          :class="{ 'active': index === selectedIndex }"
          tabindex="0"
          class="suggestion-item"
        >
          <span class="suggestion-icon">📍</span>
          <span class="suggestion-text">{{ suggestion }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'

// Props
const props = defineProps({
  modelValue: String,
  placeholder: String,
  suggestions: Array,
  isLoading: Boolean,
  debounceMs: { type: Number, default: 300 }
})

// Emits
const emit = defineEmits([
  'update:modelValue',
  'search',
  'clear',
  'select'   // ⭐ viktig for navigasjon
])

// State
const searchInput = ref(null)
const isFocused = ref(false)
const showSuggestions = ref(false)
const selectedIndex = ref(-1)
let debounceTimeout = null

// INPUT + debounce
const handleInput = (event) => {
  const value = event.target.value
  emit('update:modelValue', value)
  selectedIndex.value = -1

  clearTimeout(debounceTimeout)
  debounceTimeout = setTimeout(() => {
    emit('search', value)
    showSuggestions.value = true
  }, props.debounceMs)
}

// Velge forslag (brukes ved klikk + enter)
const selectSuggestion = (suggestion) => {
  emit('update:modelValue', suggestion)
  emit('select', suggestion)  // ⭐ sender signal til parent
  showSuggestions.value = false
}

// Clear
const clearSearch = () => {
  emit('update:modelValue', '')
  emit('clear')
  selectedIndex.value = -1
  showSuggestions.value = false
  searchInput.value?.focus()
}

// Klikk utenfor → lukk
const closeIfClickedOutside = (e) => {
  if (!searchInput.value) return
  if (!searchInput.value.parentNode.contains(e.target)) {
    showSuggestions.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', closeIfClickedOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeIfClickedOutside)
})

// Tastaturnavigasjon
const handleKeydown = (e) => {
  if (!showSuggestions.value) return

  if (e.key === 'ArrowDown') {
    selectedIndex.value = Math.min(selectedIndex.value + 1, props.suggestions.length - 1)
  }
  if (e.key === 'ArrowUp') {
    selectedIndex.value = Math.max(selectedIndex.value - 1, 0)
  }
  if (e.key === 'Enter' && selectedIndex.value >= 0) {
    selectSuggestion(props.suggestions[selectedIndex.value])
  }
  if (e.key === 'Escape') {
    showSuggestions.value = false
  }
}

// Blur delay (så man kan klikke forslag)
const handleBlur = () => {
  setTimeout(() => {
    isFocused.value = false
    showSuggestions.value = false
  }, 200)
}

// Hvis input tømmes → skjul dropdown
watch(() => props.modelValue, (newVal) => {
  if (!newVal) {
    showSuggestions.value = false
  }
})
</script>

<style scoped>
.search-bar-container {
  position: relative;
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
}

.search-icon {
  color: var(--color-secondary);
}

.search-input {
  flex: 1;
  border: none;
  font-size: 1rem;
}

.clear-button {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-secondary);
}

.loading-indicator {
  display: flex;
  align-items: center;
}

.mini-spinner {
  border: 2px solid #ddd;
  border-top: 2px solid var(--color-primary);
  border-radius: 50%;
  width: 18px;
  height: 18px;
  animation: spin 0.8s linear infinite;
}

.suggestions-dropdown {
  position: absolute;
  top: calc(100% + 6px);
  left: 0;
  right: 0;
  max-height: 260px;
  overflow-y: auto;
  z-index: 10;
}

.suggestion-item {
  display: flex;
  align-items: center;
  padding: 10px 14px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.suggestion-item:hover,
.suggestion-item.active {
  background-color: #f3f3f3;
}

.suggestion-icon {
  font-size: 1.2rem;
  margin-right: 8px;
}
</style>