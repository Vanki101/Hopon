<template>
  <ul class="favorites-list">
    <li
      v-for="fav in favorites"
      :key="fav.id"
      class="favorites-item"
    >
      <div class="fav-info">
        <p class="fav-route">
          {{ fav.from }} → {{ fav.to }}
        </p>
        <p v-if="fav.createdAt" class="fav-meta">
          Lagret: {{ formatDate(fav.createdAt) }}
        </p>
      </div>

      <div class="fav-actions">
        <button class="btn-primary" @click="$emit('use', fav)">
          Bruk dette søket
        </button>
        <button class="btn-ghost" @click="$emit('delete', fav.id)">
          Slett
        </button>
      </div>
    </li>
  </ul>
</template>

<script setup>
const props = defineProps({
  favorites: {
    type: Array,
    default: () => [],
  },
})

const formatDate = (isoString) => {
  if (!isoString) return ''
  try {
    const d = new Date(isoString)
    return d.toLocaleString('nb-NO')
  } catch {
    return isoString
  }
}
</script>

<style scoped>
.favorites-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.favorites-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #ffffff;
  border-radius: 12px;
  padding: 14px 18px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  gap: 12px;
}

.fav-info {
  flex: 1;
}

.fav-route {
  font-weight: 600;
  margin: 0 0 4px;
  color: #222;
}

.fav-meta {
  font-size: 0.85rem;
  color: #777;
  margin: 0;
}

.fav-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.btn-primary {
  background: #cf7012;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 600;
}

.btn-primary:hover {
  background: #b55f15;
}

.btn-ghost {
  background: transparent;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 8px 10px;
  cursor: pointer;
  font-size: 0.85rem;
  color: #555;
}

.btn-ghost:hover {
  background: #f5f5f5;
}

@media (max-width: 640px) {
  .favorites-item {
    flex-direction: column;
    align-items: flex-start;
  }
  .fav-actions {
    align-self: stretch;
    justify-content: flex-end;
  }
}
</style>