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
        @focus="handleFocus"
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
        type="button"
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

    <!-- Forslag dropdown -->
    <Transition name="dropdown">
      <div 
        v-if="showSuggestions && suggestions.length > 0" 
        class="suggestions-dropdown"
        @mouseenter="isMouseOverSuggestions = true"
        @mouseleave="isMouseOverSuggestions = false"
      >
        <ul role="listbox">
          <li
            v-for="(suggestion, index) in suggestions"
            :key="index"
            @mousedown.prevent="selectSuggestion(suggestion)"
            :class="{ 'active': index === selectedIndex }"
            tabindex="0"
            class="suggestion-item"
            role="option"
          >
            <span class="suggestion-icon">📍</span>
            <span class="suggestion-text">{{ suggestion }}</span>
          </li>
        </ul>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  modelValue: String,
  placeholder: String,
  suggestions: Array,
  isLoading: Boolean,
  debounceMs: { type: Number, default: 300 }
})

const emit = defineEmits([
  'update:modelValue',
  'search',
  'clear',
  'select'
])

// State
const searchInput = ref(null)
const isFocused = ref(false)
const showSuggestions = ref(false)
const selectedIndex = ref(-1)
const isMouseOverSuggestions = ref(false)
let debounceTimeout = null

// INPUT + debounce
const handleInput = (event) => {
  const value = event.target.value
  emit('update:modelValue', value)
  selectedIndex.value = -1

  clearTimeout(debounceTimeout)
  
  if (!value.trim()) {
    showSuggestions.value = false
    return
  }

  // Vis dropdown med en gang hvis vi fortsatt skriver
  if (props.suggestions.length > 0) {
    showSuggestions.value = true
  }

  debounceTimeout = setTimeout(() => {
    emit('search', value)
  }, props.debounceMs)
}

// Focus handler
const handleFocus = () => {
  isFocused.value = true
  if (props.modelValue && props.suggestions.length > 0) {
    showSuggestions.value = true
  }
}

// Velge forslag
const selectSuggestion = (suggestion) => {
  emit('update:modelValue', suggestion)
  emit('select', suggestion)
  showSuggestions.value = false
  selectedIndex.value = -1
  searchInput.value?.blur()
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
  const container = searchInput.value.closest('.search-bar-container')
  if (container && !container.contains(e.target)) {
    showSuggestions.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', closeIfClickedOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeIfClickedOutside)
  clearTimeout(debounceTimeout)
})

// Tastaturnavigasjon
const handleKeydown = (e) => {
  if (!showSuggestions.value || props.suggestions.length === 0) return

  if (e.key === 'ArrowDown') {
    e.preventDefault()
    selectedIndex.value = Math.min(selectedIndex.value + 1, props.suggestions.length - 1)
  }
  if (e.key === 'ArrowUp') {
    e.preventDefault()
    selectedIndex.value = Math.max(selectedIndex.value - 1, 0)
  }
  if (e.key === 'Enter' && selectedIndex.value >= 0) {
    e.preventDefault()
    selectSuggestion(props.suggestions[selectedIndex.value])
  }
  if (e.key === 'Escape') {
    showSuggestions.value = false
    searchInput.value?.blur()
  }
}

// Forbedret Blur-håndtering
const handleBlur = () => {
  setTimeout(() => {
    if (!isMouseOverSuggestions.value) {
      isFocused.value = false
      showSuggestions.value = false
    }
  }, 150)
}

// Watch modelValue
watch(() => props.modelValue, (newVal) => {
  if (!newVal || newVal.trim() === '') {
    showSuggestions.value = false
  }
})

// Auto-vis dropdown når suggestions kommer
watch(() => props.suggestions, (newSuggestions) => {
  if (newSuggestions && newSuggestions.length > 0 && props.modelValue && isFocused.value) {
    showSuggestions.value = true
  } else if (!newSuggestions || newSuggestions.length === 0) {
    showSuggestions.value = false
  }
})
</script>

<style scoped>
.search-bar-container {
  position: relative;
  width: 100%;
  margin: 0 auto;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 2px solid transparent;
  transition: all 0.2s ease;
}

.search-bar:focus-within {
  border-color: #cf7012;
  box-shadow: 0 4px 12px rgba(207, 112, 18, 0.15);
}

.search-icon {
  color: #666;
  display: flex;
  align-items: center;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 1rem;
  color: #222;
  background: transparent;
}

.search-input::placeholder {
  color: #999;
}

.clear-button {
  background: none;
  border: none;
  cursor: pointer;
  color: #999;
  padding: 4px;
  display: flex;
  align-items: center;
  transition: color 0.2s;
}

.clear-button:hover {
  color: #cf7012;
}

.loading-indicator {
  display: flex;
  align-items: center;
}

.mini-spinner {
  border: 2px solid #f3f3f3;
  border-top: 2px solid #cf7012;
  border-radius: 50%;
  width: 18px;
  height: 18px;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.suggestions-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  right: 0;
  max-height: 300px;
  overflow-y: auto;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  z-index: 1000;
  border: 1px solid #e5e5e5;
}

.suggestions-dropdown ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.suggestion-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
  transition: all 0.15s ease;
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-item:hover,
.suggestion-item.active {
  background-color: #fff3eb;
}

.suggestion-icon {
  font-size: 1.2rem;
  margin-right: 12px;
}

.suggestion-text {
  color: #222;
  font-size: 0.95rem;
}

/* Dropdown animation */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s ease;
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-5px);
}

/* Scrollbar styling */
.suggestions-dropdown::-webkit-scrollbar {
  width: 6px;
}

.suggestions-dropdown::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.suggestions-dropdown::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 10px;
}

.suggestions-dropdown::-webkit-scrollbar-thumb:hover {
  background: #999;
}
</style>