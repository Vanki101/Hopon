<template>
  <div id="app">
    <!-- Enkel navigasjon for testing -->
    <nav class="navbar">
      <router-link to="/">Hjem</router-link>
      <router-link to="/search">Søk</router-link>
      <router-link to="/trip">Reiser</router-link>
      <router-link to="/tickets">Billetter</router-link>
      <router-link to="/settings">Innstillinger</router-link>
    </nav>

    <!-- Her vises views basert på rute -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
// Ingen logikk her — alt håndteres i views
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* Enkel navigasjonsmeny (kan styles i style.css senere) */
.navbar {
  display: flex;
  justify-content: center;
  gap: 20px;
  background: linear-gradient(90deg, #2EA3A3, #D46A1C);
  padding: 16px 0;
  font-weight: 600;
}

.navbar a {
  color: white;
  text-decoration: none;
  font-size: 16px;
  transition: opacity 0.2s ease;
}

.navbar a:hover {
  opacity: 0.8;
}

.main-content {
  flex: 1;
  padding: 24px;
}
</style>
