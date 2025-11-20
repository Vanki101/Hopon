<template>
  <section class="tickets-view">
    <header class="tickets-header">
      <h1>Dine billetter</h1>
      <p>Skann QR-koden for å validere billetten din.</p>
    </header>

    <!-- Ingen billetter -->
    <div v-if="!tickets.length" class="empty">
      <p>Du har ingen billetter ennå.</p>
      <button class="buy-btn" @click="handleBuy">Kjøp billett</button>
    </div>

    <!-- Liste over billetter -->
    <div v-else class="tickets-list">
      <div
        v-for="ticket in tickets"
        :key="ticket.id"
        class="ticket-card"
        @click="viewTicket(ticket)"
        tabindex="0"
        role="button"
      >
        <div class="ticket-top">
          <div>
            <h3 class="route">{{ ticket.from }} → {{ ticket.to }}</h3>
            <p class="meta">
              Linje <strong>{{ ticket.line }}</strong> · Avgang {{ ticket.departureTime }}
            </p>
          </div>
          <span class="price">{{ ticket.price }} kr</span>
        </div>

        <div class="ticket-bottom">
          <div class="qr-section">
            <img
              :src="generateQR(ticket)"
              alt="QR kode for billett"
              class="qr-image"
            />
            <span class="qr-label">Skann for validering</span>
          </div>

          <div class="ticket-status" :class="ticket.active ? 'active' : 'expired'">
            {{ ticket.active ? 'Aktiv billett' : 'Utløpt billett' }}
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Midlertidige billetter (mock)
const tickets = ref([
  { id: 1, from: 'Halden', to: 'Sarpsborg', line: '3', departureTime: '12:05', price: 58, active: true },
  { id: 2, from: 'Halden', to: 'Fredrikstad', line: '7', departureTime: '13:10', price: 65, active: false },
])

// Generer QR (API-basert)
const generateQR = (ticket) => {
  const data = `HopOn-${ticket.id}-${ticket.from}-${ticket.to}-${ticket.line}-${ticket.departureTime}`
  return `https://api.qrserver.com/v1/create-qr-code/?size=120x120&data=${encodeURIComponent(data)}`
}

// Naviger til detaljvisning
const viewTicket = (ticket) => {
  router.push({
    name: 'trip',
    query: {
      id: ticket.id,
      from: ticket.from,
      to: ticket.to,
      line: ticket.line,
      departureTime: ticket.departureTime,
      price: ticket.price,
    },
  })
}

// Kjøp-knapp (placeholder)
const handleBuy = () => {
  alert('Billettkjøp kommer snart 🚀')
}
</script>

<style scoped>
.tickets-view {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px 80px;
}

.tickets-header {
  text-align: center;
  margin-bottom: 40px;
}

.tickets-header h1 {
  color: var(--color-primary);
  font-size: 2rem;
  margin-bottom: 8px;
}

.tickets-header p {
  color: #555;
}

/* TOM LISTE */
.empty {
  text-align: center;
  background: #fff;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.buy-btn {
  margin-top: 20px;
  background: #cf7012;
  color: #fff;
  border: none;
  padding: 12px 20px;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s ease;
}
.buy-btn:hover { background: #b55f15; }

/* BILLETTLISTE */
.tickets-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.ticket-card {
  background: #fff;
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  transition: transform 0.2s ease;
  cursor: pointer;
}
.ticket-card:hover {
  transform: translateY(-2px);
  background: #fffaf5;
}

.ticket-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.route {
  font-size: 1.2rem;
  font-weight: 600;
  color: #222;
  margin: 0;
}
.meta {
  margin-top: 4px;
  font-size: 0.9rem;
  color: #555;
}
.price {
  font-weight: 700;
  color: #000;
}

/* QR OG STATUS */
.ticket-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.qr-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.qr-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  background: #fafafa;
  padding: 6px;
}
.qr-label {
  font-size: 0.8rem;
  color: #666;
  margin-top: 4px;
}

.ticket-status {
  font-weight: 600;
  font-size: 0.9rem;
  padding: 6px 12px;
  border-radius: 8px;
  text-align: center;
}
.ticket-status.active {
  background: #e4f8e4;
  color: #1b8a1b;
}
.ticket-status.expired {
  background: #f8e4e4;
  color: #a11b1b;
}

/* RESPONSIV */
@media (max-width: 600px) {
  .ticket-bottom {
    flex-direction: column;
    align-items: center;
    gap: 16px;
  }
}
</style>
