<template>
  <section class="stop-view">
    <h1>Avganger</h1>

    <!-- Søkefelt -->
    <SearchBar
      v-model="stopName"
      placeholder="Søk etter holdeplass…"
      :suggestions="suggestions"
      :isLoading="isLoadingSuggestions"
      @search="handleSearch"
      @select="handleSelect"
      @clear="handleClear"
    />

    <button
      class="load-btn"
      :disabled="!stopName || isLoading"
      @click="loadDepartures"
    >
      {{ isLoading ? "Laster…" : "Hent avganger" }}
    </button>

    <p v-if="error" class="error-text">{{ error }}</p>

    <!-- Resultatliste -->
    <ul v-if="departures.length" class="departure-list">
      <li v-for="(dep, i) in departures" :key="i" class="departure-item">
        <strong>{{ dep.line }}</strong>
        <span>{{ formatTime(dep.expectedDepartureTime) }}</span>
        <em>{{ dep.destinationDisplay }}</em>
      </li>
    </ul>

    <p v-else-if="stopName && !isLoading" class="empty-state">Ingen avganger funnet.</p>
  </section>
</template>

<script setup>
import { ref } from "vue";
import SearchBar from "../components/SearchBar.vue";
import { api } from "../api/endpoints.js";

// STATE
const stopName = ref("");
const suggestions = ref([]);
const departures = ref([]);
const error = ref("");

const isLoading = ref(false);
const isLoadingSuggestions = ref(false);

// ---- Søke-forslag fra backend (`/search`) ----
const handleSearch = async (query) => {
  if (!query) return (suggestions.value = []);

  isLoadingSuggestions.value = true;

  try {
    const res = await api.searchStops(query);
    suggestions.value = res.map((s) => s.name);
  } catch (e) {
    console.error(e);
  } finally {
    isLoadingSuggestions.value = false;
  }
};

const handleSelect = (value) => {
  stopName.value = value;
  suggestions.value = [];
};

const handleClear = () => {
  stopName.value = "";
  suggestions.value = [];
};

// ---- Hent avganger fra backend (`/departures`) ----
const loadDepartures = async () => {
  if (!stopName.value) return;

  isLoading.value = true;
  error.value = "";
  departures.value = [];

  try {
    const res = await api.getDepartures(stopName.value);
    departures.value = res;
  } catch (e) {
    console.error(e);
    error.value = "Kunne ikke hente avganger.";
  } finally {
    isLoading.value = false;
  }
};

// ---- Format tid ----
const formatTime = (iso) => {
  try {
    return new Date(iso).toLocaleTimeString("nb-NO", {
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch {
    return iso;
  }
};
</script>

<style scoped>
.stop-view {
  max-width: 700px;
  margin: 40px auto;
  background: #fff;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
}

.load-btn {
  background: #cf7012;
  color: #fff;
  padding: 12px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  margin-top: 12px;
}

.error-text {
  color: #d15555;
}

.departure-list {
  list-style: none;
  padding: 0;
  margin-top: 24px;
}

.departure-item {
  background: #fafafa;
  padding: 14px 18px;
  border-radius: 10px;
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
}
</style>
message.txt
4 KB