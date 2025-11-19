<template>
  <section class="trip-view">
    <div class="trip-card" v-if="trip">
      <!-- Header -->
      <div class="trip-header">
        <div class="line-badge-large">{{ trip.line }}</div>
        <h1 class="trip-title">{{ trip.from }} → {{ trip.to }}</h1>
      </div>

      <!-- Reisedetaljer -->
      <div class="trip-details-grid">
        <!-- Avgangstid -->
        <div class="detail-card">
          <div class="detail-icon departure-icon">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="24" height="24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zM3.75 12h.007v.008H3.75V12zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z" />
            </svg>
          </div>
          <div class="detail-content">
            <span class="detail-label">Avgang</span>
            <span class="detail-value">{{ trip.departureTime }}</span>
          </div>
        </div>

        <!-- Ankomsttid -->
        <div class="detail-card">
          <div class="detail-icon arrival-icon">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="24" height="24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div class="detail-content">
            <span class="detail-label">Ankomst</span>
            <span class="detail-value">{{ trip.arrivalTime }}</span>
          </div>
        </div>

        <!-- Varighet -->
        <div class="detail-card">
          <div class="detail-icon duration-icon">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="24" height="24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div class="detail-content">
            <span class="detail-label">Varighet</span>
            <span class="detail-value">{{ trip.duration }} minutter</span>
          </div>
        </div>

        <!-- Pris (hvis tilgjengelig) -->
        <div class="detail-card" v-if="trip.price">
          <div class="detail-icon price-icon">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="24" height="24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 013 6h-.75m0 0v-.375c0-.621.504-1.125 1.125-1.125H20.25M2.25 6v9m18-10.5v.75c0 .414.336.75.75.75h.75m-1.5-1.5h.375c.621 0 1.125.504 1.125 1.125v9.75c0 .621-.504 1.125-1.125 1.125h-.375m1.5-1.5H21a.75.75 0 00-.75.75v.75m0 0H3.75m0 0h-.375a1.125 1.125 0 01-1.125-1.125V15m1.5 1.5v-.75A.75.75 0 003 15h-.75M15 10.5a3 3 0 11-6 0 3 3 0 016 0zm3 0h.008v.008H18V10.5zm-12 0h.008v.008H6V10.5z" />
            </svg>
          </div>
          <div class="detail-content">
            <span class="detail-label">Pris</span>
            <span class="detail-value">{{ trip.price }} kr</span>
          </div>
        </div>
      </div>

      <!-- Info box -->
      <div class="info-box">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="20" height="20">
          <path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" />
        </svg>
        <p>Husk å være på holdeplassen i god tid før avgang!</p>
      </div>

      <!-- Reisens etapper (legs) -->
      <div v-if="trip.legs && trip.legs.length > 0" class="trip-legs">
        <h2 class="section-title">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="22" height="22">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 6.75V15m6-6v8.25m.503 3.498l4.875-2.437c.381-.19.622-.58.622-1.006V4.82c0-.836-.88-1.38-1.628-1.006l-3.869 1.934c-.317.159-.69.159-1.006 0L9.503 3.252a1.125 1.125 0 00-1.006 0L3.622 5.689C3.24 5.88 3 6.27 3 6.695V19.18c0 .836.88 1.38 1.628 1.006l3.869-1.934c.317-.159.69-.159 1.006 0l4.994 2.497c.317.158.69.158 1.006 0z" />
          </svg>
          Reiseroute
        </h2>

        <div class="legs-container">
          <div 
            v-for="(leg, index) in trip.legs" 
            :key="index" 
            class="leg-card"
          >
            <!-- Leg header -->
            <div class="leg-header">
              <div class="leg-mode" :class="`mode-${leg.mode.toLowerCase()}`">
                <span v-if="leg.mode === 'bus'">🚌</span>
                <span v-else-if="leg.mode === 'foot'">🚶</span>
                <span v-else-if="leg.mode === 'rail'">🚆</span>
                <span v-else-if="leg.mode === 'tram'">🚊</span>
                <span v-else-if="leg.mode === 'metro'">🚇</span>
                <span v-else>🚌</span>
                <span class="leg-mode-text">
                  {{ leg.mode === 'foot' ? 'Gå' : `Linje ${leg.line || leg.mode}` }}
                </span>
              </div>
              <span class="leg-duration">{{ leg.duration }} min</span>
            </div>

            <!-- From -->
            <div class="leg-stop">
              <div class="stop-dot start-dot"></div>
              <div class="stop-info">
                <div class="stop-name">{{ leg.fromPlace }}</div>
                <div class="stop-details">
                  <span class="stop-time">{{ leg.departureTime }}</span>
                  <span v-if="leg.fromQuay" class="stop-quay">Plattform {{ leg.fromQuay }}</span>
                </div>
              </div>
            </div>

            <!-- Connector line -->
            <div class="leg-connector"></div>

            <!-- To -->
            <div class="leg-stop">
              <div class="stop-dot end-dot"></div>
              <div class="stop-info">
                <div class="stop-name">{{ leg.toPlace }}</div>
                <div class="stop-details">
                  <span class="stop-time">{{ leg.arrivalTime }}</span>
                  <span v-if="leg.toQuay" class="stop-quay">Plattform {{ leg.toQuay }}</span>
                </div>
              </div>
            </div>

            <!-- Distance info for walking -->
            <div v-if="leg.mode === 'foot' && leg.distance" class="walk-info">
              🚶 Ca. {{ Math.round(leg.distance) }} meter
            </div>
          </div>
        </div>

        <!-- Total walking distance -->
        <div v-if="trip.walkDistance > 0" class="walk-summary">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="18" height="18">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
          </svg>
          Total gåavstand: {{ trip.walkDistance }} meter
        </div>
      </div>

      <!-- Actions -->
      <div class="trip-actions">
        <button class="back-btn" @click="goBack">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" width="20" height="20">
            <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5L3 12m0 0l7.5-7.5M3 12h18" />
          </svg>
          Tilbake til søk
        </button>
      </div>
    </div>

    <div v-else class="empty-view">
      <div class="empty-content">
        <span class="empty-icon">🚌</span>
        <p>Ingen reise valgt.</p>
        <button class="back-btn" @click="goBack">Gå til søk</button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'

const router = useRouter()

// Trip data
const trip = ref(null)

onMounted(() => {
  // Hent trip data fra localStorage
  const savedTrip = localStorage.getItem('selectedTrip')
  
  if (savedTrip) {
    try {
      trip.value = JSON.parse(savedTrip)
      console.log('✅ Trip data loaded:', trip.value)
    } catch (e) {
      console.error('❌ Error parsing trip data:', e)
    }
  } else {
    console.warn('⚠️ No trip data found in localStorage')
  }
})

// Naviger tilbake til søk
const goBack = () => {
  router.push({ name: 'home' })  // Endret fra 'search' til 'home'
}
</script>

<style scoped>
.trip-view {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px 80px;
}

.trip-card {
  background: #fff;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

/* Header */
.trip-header {
  text-align: center;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 2px solid #f5f5f5;
}

.line-badge-large {
  display: inline-block;
  background: linear-gradient(135deg, #2ea3a3 0%, #258f8f 100%);
  color: white;
  font-weight: 700;
  border-radius: 12px;
  padding: 12px 24px;
  font-size: 1.5rem;
  box-shadow: 0 4px 10px rgba(46, 163, 163, 0.25);
  margin-bottom: 16px;
}

.trip-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #222;
  margin: 0;
}

/* Details Grid */
.trip-details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.detail-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 12px;
  border: 2px solid transparent;
  transition: all 0.2s ease;
}

.detail-card:hover {
  border-color: #cf7012;
  background: #fff;
}

.detail-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.departure-icon {
  background: linear-gradient(135deg, #4a90e2 0%, #357abd 100%);
}

.arrival-icon {
  background: linear-gradient(135deg, #2ea3a3 0%, #258f8f 100%);
}

.duration-icon {
  background: linear-gradient(135deg, #cf7012 0%, #b85f15 100%);
}

.price-icon {
  background: linear-gradient(135deg, #7b61ff 0%, #6347e8 100%);
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-label {
  font-size: 0.85rem;
  color: #999;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-value {
  font-size: 1.1rem;
  font-weight: 600;
  color: #222;
}

/* Info Box */
.info-box {
  background: #fff3eb;
  border-left: 4px solid #cf7012;
  padding: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.info-box svg {
  color: #cf7012;
  flex-shrink: 0;
}

.info-box p {
  margin: 0;
  color: #666;
  font-size: 0.95rem;
}

/* Actions */
.trip-actions {
  display: flex;
  justify-content: center;
}

.back-btn {
  background: linear-gradient(135deg, #cf7012 0%, #b85f15 100%);
  color: #fff;
  border: none;
  padding: 12px 24px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  font-size: 1rem;
  transition: all 0.2s ease;
  box-shadow: 0 4px 10px rgba(207, 112, 18, 0.2);
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-btn:hover {
  background: linear-gradient(135deg, #b85f15 0%, #a05413 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 14px rgba(207, 112, 18, 0.3);
}

/* Empty View */
.empty-view {
  background: #fff;
  padding: 60px 32px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  text-align: center;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  display: block;
  margin-bottom: 20px;
}

.empty-content p {
  color: #666;
  font-size: 1.1rem;
  margin-bottom: 24px;
}

/* Responsive */
@media (max-width: 600px) {
  .trip-card {
    padding: 24px;
  }

  .trip-title {
    font-size: 1.5rem;
  }

  .trip-details-grid {
    grid-template-columns: 1fr;
  }
}

/* Legs styling */
.trip-legs {
  margin-top: 32px;
  padding-top: 32px;
  border-top: 2px solid #f5f5f5;
}

.section-title {
  font-size: 1.4rem;
  font-weight: 700;
  color: #222;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.section-title svg {
  color: #cf7012;
}

.legs-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.leg-card {
  background: #f9f9f9;
  border-radius: 12px;
  padding: 20px;
  border-left: 4px solid #cf7012;
}

.leg-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.leg-mode {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 1.1rem;
}

.leg-mode span:first-child {
  font-size: 1.5rem;
}

.leg-mode-text {
  color: #222;
}

.leg-duration {
  background: #fff;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.9rem;
  color: #666;
  font-weight: 500;
}

.leg-stop {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 8px 0;
}

.stop-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  flex-shrink: 0;
  margin-top: 4px;
}

.start-dot {
  background: #4a90e2;
  border: 3px solid #357abd;
}

.end-dot {
  background: #2ea3a3;
  border: 3px solid #258f8f;
}

.stop-info {
  flex: 1;
}

.stop-name {
  font-weight: 600;
  color: #222;
  font-size: 1rem;
  margin-bottom: 4px;
}

.stop-details {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.stop-time {
  color: #cf7012;
  font-weight: 600;
  font-size: 0.95rem;
}

.stop-quay {
  background: #fff;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 0.85rem;
  color: #666;
  border: 1px solid #e5e5e5;
}

.leg-connector {
  width: 2px;
  height: 24px;
  background: linear-gradient(to bottom, #ddd 0%, #ddd 100%);
  margin-left: 7px;
}

.walk-info {
  margin-top: 12px;
  padding: 10px 14px;
  background: #fff3eb;
  border-radius: 8px;
  font-size: 0.9rem;
  color: #666;
}

.walk-summary {
  margin-top: 20px;
  padding: 14px;
  background: #fff;
  border-radius: 10px;
  border: 2px solid #f5f5f5;
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
  color: #666;
}

.walk-summary svg {
  color: #cf7012;
}

.mode-foot {
  color: #cf7012;
}

.mode-bus {
  color: #2ea3a3;
}

.mode-rail {
  color: #4a90e2;
}

.mode-tram {
  color: #7b61ff;
}
</style>