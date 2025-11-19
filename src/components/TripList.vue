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
        <span class="line-badge">{{ trip.line }}</span>
      </div>

      <!-- Info -->
      <div class="trip-info">
        <h3 class="trip-route">{{ trip.from }} → {{ trip.to }}</h3>
        <div class="trip-details">
          <span class="trip-time">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="16" height="16">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {{ trip.departureTime }} - {{ trip.arrivalTime }}
          </span>
          <span class="trip-duration">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="16" height="16">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6l4 2" />
            </svg>
            {{ trip.duration }} min
          </span>
        </div>
      </div>

      <!-- Pil -->
      <div class="trip-arrow">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="20" height="20">
          <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
        </svg>
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
  padding: 18px 20px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.trip-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: #cf7012;
  transform: scaleY(0);
  transition: transform 0.2s ease;
}

.trip-item:hover {
  background-color: #fff;
  border-color: #cf7012;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(207, 112, 18, 0.15);
}

.trip-item:hover::before {
  transform: scaleY(1);
}

.trip-item:active {
  transform: translateX(2px) scale(0.99);
}

.trip-item:focus {
  outline: none;
  border-color: #cf7012;
  box-shadow: 0 0 0 3px rgba(207, 112, 18, 0.1);
}

/* Linje */
.trip-line {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 60px;
}

.line-badge {
  background: linear-gradient(135deg, #2ea3a3 0%, #258f8f 100%);
  color: white;
  font-weight: 700;
  border-radius: 8px;
  padding: 8px 12px;
  font-size: 1.1rem;
  box-shadow: 0 2px 6px rgba(46, 163, 163, 0.25);
  min-width: 50px;
  text-align: center;
}

/* Info */
.trip-info {
  flex: 1;
  margin-left: 20px;
  margin-right: 16px;
}

.trip-route {
  font-size: 1.1rem;
  font-weight: 600;
  color: #222;
  margin: 0 0 8px;
}

.trip-details {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.trip-time,
.trip-duration {
  font-size: 0.9rem;
  color: #666;
  display: flex;
  align-items: center;
  gap: 6px;
}

.trip-time svg,
.trip-duration svg {
  color: #cf7012;
}

/* Arrow */
.trip-arrow {
  color: #999;
  display: flex;
  align-items: center;
  transition: all 0.2s ease;
}

.trip-item:hover .trip-arrow {
  color: #cf7012;
  transform: translateX(4px);
}

/* Responsiv */
@media (max-width: 600px) {
  .trip-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .trip-line {
    width: 100%;
    justify-content: flex-start;
  }

  .trip-info {
    margin-left: 0;
    width: 100%;
  }

  .trip-arrow {
    display: none;
  }

  .trip-details {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>