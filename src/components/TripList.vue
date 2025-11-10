<template>
  <div class="trip-list">
    <div
      v-for="trip in trips"
      :key="trip.id"
      class="trip-item"
      tabindex="0"
      role="button"
      @click="$emit('select', trip)" 
      @keydown.enter="$emit('select', trip)"
    >
      <!-- Linje-nummer -->
      <div class="trip-line">
        <span class="line-number">{{ trip.line }}</span>
      </div>

      <!-- Info -->
      <div class="trip-info">
        <h3 class="trip-route">{{ trip.from }} → {{ trip.to }}</h3>
        <p class="trip-time">
          Avgang {{ trip.departureTime }} · Ankomst {{ trip.arrivalTime }} ({{ trip.duration }} min)
        </p>
      </div>

      <!-- Pris -->
      <div class="trip-price">
        <span>{{ trip.price }} kr</span>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  trips: {
    type: Array,
    default: () => [],
  },
})
defineEmits(['select'])
</script>

<style scoped>
.trip-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* Hver reise */
.trip-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f9f9f9;
  padding: 16px 20px;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.1s;
  border: 1px solid #eee;
}

.trip-item:hover {
  background-color: #fff3eb;
  transform: translateY(-1px);
}

.trip-item:active {
  transform: scale(0.99);
}

/* Linje */
.trip-line {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e9f3f3;
  color: #2ea3a3;
  font-weight: 600;
  border-radius: 8px;
  padding: 8px 14px;
  min-width: 40px;
  text-align: center;
  font-size: 1rem;
}

/* Info */
.trip-info {
  flex: 1;
  margin-left: 16px;
}

.trip-route {
  font-size: 1.05rem;
  font-weight: 600;
  color: #222;
  margin: 0 0 4px;
}

.trip-time {
  font-size: 0.9rem;
  color: #555;
  margin: 0;
}

/* Pris */
.trip-price {
  font-weight: 700;
  color: #111;
  font-size: 1rem;
  margin-left: 12px;
}

/* Responsiv */
@media (max-width: 600px) {
  .trip-item {
    flex-direction: column;
    align-items: flex-start;
  }
  .trip-price {
    align-self: flex-end;
    margin-top: 8px;
  }
}
</style>
