<template>
  <div class="search-bar-container">
    <div class="search-bar component-card">
      <div class="search-icon">
        <svg 
          xmlns="http://www.w3.org/2000/svg" 
          fill="none" 
          viewBox="0 0 24 24" 
          stroke-width="2" 
          stroke="currentColor"
          width="20"
          height="20"
        >
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
        </svg>
      </div>
      
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

      <div v-if="isLoading" class="loading-indicator">
        <div class="mini-spinner"></div>
      </div>
    </div>

    <!-- Suggestions dropdown -->
    <div 
      v-if="showSuggestions && suggestions.length > 0" 
      class="suggestions-dropdown component-card"
    >
      <ul role="listbox">
        <li
          v-for="(suggestion, index) in suggestions"
          :key="index"
          @click="selectSuggestion(suggestion)"
          @keydown.enter="selectSuggestion(suggestion)"
          class="suggestion-item"
          role="option"
          :aria-selected="false"
          tabindex="0"
        >
          <span class="suggestion-icon"></span>
          <span class="suggestion-text">{{ suggestion }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

// Props
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: 'Søk etter holdeplass eller adresse...'
  },
  suggestions: {
    type: Array,
    default: () => []
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  debounceMs: {
    type: Number,
    default: 300
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'search', 'clear'])

// State
const searchInput = ref(null)
const isFocused = ref(false)
const showSuggestions = ref(false)
let debounceTimeout = null

// Methods
const handleInput = (event) => {
  const value = event.target.value
  emit('update:modelValue', value)
  
  // Debounce search
  clearTimeout(debounceTimeout)
  debounceTimeout = setTimeout(() => {
    emit('search', value)
    showSuggestions.value = true
  }, props.debounceMs)
}

const clearSearch = () => {
  emit('update:modelValue', '')
  emit('clear')
  showSuggestions.value = false
  searchInput.value?.focus()
}

const selectSuggestion = (suggestion) => {
  emit('update:modelValue', suggestion)
  emit('search', suggestion)
  showSuggestions.value = false
}

const handleBlur = () => {
  // Delay to allow click on suggestions
  setTimeout(() => {
    isFocused.value = false
    showSuggestions.value = false
  }, 2000)
}

// Watch for external changes
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
  gap: var(--spacing-sm);
  padding: var(--spacing-sm);
  position: relative;
}

.search-icon {
  color: var(--color-secondary);
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  padding: var(--spacing-sm);
  font-size: var(--font-size-base);
}

.search-input:focus {
  box-shadow: none;
}

.clear-button {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-secondary);
  padding: var(--spacing-xs);
  display: flex;
  align-items: center;
  transition: color 0.2s ease;
}

.clear-button:hover {
  color: var(--color-danger);
}

.clear-button:focus {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
  border-radius: 4px;
}

.loading-indicator {
  display: flex;
  align-items: center;
}

.mini-spinner {
  border: 2px solid var(--color-gray-200);
  border-top: 2px solid var(--color-primary);
  border-radius: 50%;
  width: 20px;
  height: 20px;
  animation: spin 0.8s linear infinite;
}

.suggestions-dropdown {
  position: absolute;
  top: calc(100% + var(--spacing-sm));
  left: 0;
  right: 0;
  max-height: 300px;
  overflow-y: auto;
  z-index: 10;
  padding: 0;
}

.suggestions-dropdown ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.suggestion-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-md);
  cursor: pointer;
  transition: background-color 0.2s ease;
  border-bottom: 1px solid var(--color-gray-200);
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-item:hover,
.suggestion-item:focus {
  background-color: var(--color-gray-100);
  outline: none;
}

.suggestion-icon {
  font-size: var(--font-size-lg);
  flex-shrink: 0;
}

.suggestion-text {
  font-size: var(--font-size-base);
  color: var(--color-gray-900);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Responsive */
@media (max-width: 768px) {
  .search-bar-container {
    max-width: 100%;
  }
  
  .search-input {
    font-size: var(--font-size-sm);
  }
}
</style>