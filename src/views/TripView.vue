<template>
  <section class="trip-view">
    <div class="trip-card" v-if="trip">
      <h1>{{ trip.from }} → {{ trip.to }}</h1>
      <p class="time">
        🕐 Avgang: <strong>{{ trip.departureTime }}</strong> —
        Ankomst: <strong>{{ trip.arrivalTime }}</strong>
      </p>
      <p>⏱ Varighet: {{ trip.duration }} minutter</p>
      <p v-if="trip.price">💰 Pris: {{ trip.price }} kr</p>

      <button class="back-btn" @click="goBack">← Tilbake</button>
    </div>

    <div v-else class="empty">
      <p>Ingen reise valgt.</p>
      <button class="back-btn" @click="goBack">Gå tilbake</button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'

// Ruter & data
const route = useRoute()
const router = useRouter()

// Hent query-parametere
const trip = computed(() => {
  if (!route.query.id) return null
  return {
    id: route.query.id as string,
    from: route.query.from as string,
    to: route.query.to as string,
    line: route.query.line as string,
    departureTime: route.query.departureTime as string,
    arrivalTime: route.query.arrivalTime as string,
    duration: Number(route.query.duration),
    price: Number(route.query.price),
  }
})

// Naviger tilbake
const goBack = () => {
  router.push({ name: 'search' }) // endre til navnet på søkesiden
}
</script>

<style scoped>
.trip-view {
  max-width: 700px;
  margin: 60px auto;
  background: #fff;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.05);
}

.trip-card h1 {
  font-size: 1.8rem;
  margin-bottom: 16px;
  color: #222;
}

.time {
  color: #444;
  margin-bottom: 10px;
}

.back-btn {
  margin-top: 24px;
  background: #d46a1c;
  color: #fff;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s ease;
}
.back-btn:hover {
  background: #b85b17;
}

.empty {
  text-align: center;
  color: #555;
}
</style>
