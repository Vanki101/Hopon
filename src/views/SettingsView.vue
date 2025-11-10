<template>
  <section class="settings-view">
    <header class="settings-header">
      <h1>Innstillinger</h1>
      <p>Administrer kontoen din, språk og tema.</p>
    </header>

    <!-- Profilkort -->
    <div class="settings-card">
      <h2>Profil</h2>
      <div class="form-group">
        <label for="name">Navn</label>
        <input id="name" v-model="user.name" type="text" placeholder="Fornavn Etternavn" />
      </div>

      <div class="form-group">
        <label for="email">E-post</label>
        <input id="email" v-model="user.email" type="email" placeholder="navn@epost.no" />
      </div>

      <button class="save-btn" @click="saveProfile">Lagre endringer</button>
    </div>

    <!-- App-innstillinger -->
    <div class="settings-card">
      <h2>App-innstillinger</h2>

      <div class="form-group">
        <label for="language">Språk</label>
        <select id="language" v-model="language">
          <option value="no">Norsk (Bokmål)</option>
          <option value="en">Engelsk</option>
        </select>
      </div>

      <div class="form-group toggle">
        <label for="theme">Mørk modus</label>
        <input id="theme" type="checkbox" v-model="darkMode" />
      </div>

      <button class="save-btn" @click="applySettings">Bruk innstillinger</button>
    </div>

    <!-- Konto-seksjon -->
    <div class="settings-card danger">
      <h2>Konto</h2>
      <p>Om du ønsker å logge ut av HopOn, kan du gjøre det her.</p>
      <button class="logout-btn" @click="logout">Logg ut</button>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

// Brukerdata (mock)
const user = ref({
  name: 'Mohamad Abbas',
  email: 'mohamad@hopon.no',
})

// States
const language = ref('no')
const darkMode = ref(false)

// Lagre profil (placeholder)
const saveProfile = () => {
  alert(`Profil oppdatert!\nNavn: ${user.value.name}\nE-post: ${user.value.email}`)
}

// Bruk innstillinger (placeholder)
const applySettings = () => {
  alert(`Innstillinger lagret: ${language.value === 'no' ? 'Norsk' : 'Engelsk'} – ${darkMode.value ? 'Mørk modus' : 'Lys modus'}`)
}

// Logg ut (placeholder)
const logout = () => {
  alert('Du er nå logget ut 👋')
}
</script>

<style scoped>
.settings-view {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px 80px;
}

.settings-header {
  text-align: center;
  margin-bottom: 40px;
}

.settings-header h1 {
  font-size: 2rem;
  color: var(--color-primary);
  margin-bottom: 8px;
}

.settings-header p {
  color: #555;
}

/* Kortlayout */
.settings-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  margin-bottom: 32px;
}

.settings-card h2 {
  margin-bottom: 16px;
  color: #222;
}

/* Formularer */
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
}

.form-group label {
  font-weight: 600;
  margin-bottom: 6px;
  color: #333;
}

.form-group input,
.form-group select {
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 1rem;
  background: #fafafa;
  transition: border 0.2s ease;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #cf7012;
  outline: none;
}

.toggle {
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.save-btn {
  background: #cf7012;
  color: #fff;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s ease;
}

.save-btn:hover {
  background: #b55f15;
}

/* Konto-seksjon */
.settings-card.danger {
  border-left: 5px solid #e03e3e;
}

.logout-btn {
  background: #e03e3e;
  color: #fff;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s ease;
}

.logout-btn:hover {
  background: #b52b2b;
}

/* Responsiv */
@media (max-width: 600px) {
  .settings-card {
    padding: 20px;
  }
}
</style>
