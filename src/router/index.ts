import { createRouter, createWebHistory } from 'vue-router'

// Importer views
import HomeView from '../views/HomeView.vue'
import SearchView from '../views/SearchView.vue'
import TripView from '../views/TripView.vue'
import StopView from '../views/StopView.vue'
import TicketsView from '../views/TicketsView.vue'
import SettingsView from '../views/SettingsView.vue'

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/search', name: 'search', component: SearchView },
  { path: '/trip', name: 'trip', component: TripView },
  { path: '/stop', name: 'stop', component: StopView },
  { path: '/tickets', name: 'tickets', component: TicketsView },
  { path: '/settings', name: 'settings', component: SettingsView },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
