import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import TripView from '../views/TripView.vue'
import StopView from '../views/StopView.vue'
import TicketsView from '../views/TicketsView.vue'
import SettingsView from '../views/SettingsView.vue'
import FavoritesView from '../views/FavoritesView.vue' 

const routes = [
  { path: '/', name: 'home', component: HomeView },        
  { path: '/trip', name: 'trip', component: TripView },
  { path: '/stop', name: 'stop', component: StopView },
  { path: '/tickets', name: 'tickets', component: TicketsView },
  { path: '/settings', name: 'settings', component: SettingsView },
  { path: '/favorites', name: 'favorites', component: FavoritesView },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router