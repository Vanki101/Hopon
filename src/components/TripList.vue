<template>
  <section class="trip-list">
    <div v-if="loading" class="state">Laster reiseforslag…</div>
    <div v-else-if="error" class="state error">⚠️ {{ error }}</div>
    <div v-else-if="!trips.length" class="state">Ingen reiser funnet.</div>

    <ul v-else class="list">
      <li
        v-for="t in trips"
        :key="t.id"
        class="item"
        tabindex="0"
        @click="$emit('select', t)"
        @keydown.enter.prevent="$emit('select', t)"
      >
        <span class="badge">{{ t.line }}</span>
        <div class="meta">
          <div class="route">{{ t.from }} → {{ t.to }}</div>
          <div class="time">Avgang {{ t.departureTime }} · Ankomst {{ t.arrivalTime }} ({{ t.duration }} min)</div>
        </div>
        <div v-if="t.price" class="price">{{ t.price }} kr</div>
      </li>
    </ul>
  </section>
</template>

<script setup lang="ts">
export interface TripItem {
  id: string
  line: string
  from: string
  to: string
  departureTime: string
  arrivalTime: string
  duration: number
  price?: number
}

withDefaults(defineProps<{
  trips: TripItem[]
  loading?: boolean
  error?: string | null
}>(), {
  trips: () => [],
  loading: false,
  error: null,
})
</script>

<style scoped>
.trip-list { width: 100%; }
.state { padding: 12px 0; color: #555; }
.state.error { color: #b00020; }

.list { list-style: none; margin: 0; padding: 0; border-top: 1px solid #eee; }
.item {
  display: grid;
  grid-template-columns: auto 1fr auto;
  gap: 12px;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.item:hover, .item:focus { background: #f7f9fb; outline: none; }

.badge {
  min-width: 36px; text-align: center;
  padding: 2px 8px; border-radius: 8px;
  background: #e6f1f8; color: #0f4a6c; font-weight: 700; font-size: 12px;
}
.meta { display: grid; gap: 4px; }
.route { font-weight: 600; }
.time { font-size: 13px; color: #666; }
.price { font-weight: 700; }
</style>
