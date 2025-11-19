// src/api/backend.js

const BASE_URL = 'http://localhost:8080'; // samme som backend

export const api = {
  // Søk etter stoppesteder (brukes til forslag i SearchBar om du vil)
  async searchStops(name) {
    const response = await fetch(
      `${BASE_URL}//search?name=${encodeURIComponent(name)}`
    );
    if (!response.ok) throw new Error('Søk etter stoppesteder feilet');
    return response.json();
  },

  // Hent avganger for en holdeplass (brukes evt senere)
  async getDepartures(nameOrId) {
    const param = nameOrId.startsWith('NSR:')
      ? `id=${nameOrId}`
      : `name=${encodeURIComponent(nameOrId)}`;

    const response = await fetch(`${BASE_URL}/departures?${param}`);
    if (!response.ok) throw new Error('Kunne ikke hente avganger');
    return response.json();
  },

  // Planlegg reise (HOVEDGREIA: fra + til)
  async planTrip(from, to) {
    const response = await fetch(
      `${BASE_URL}/trip?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`
    );
    if (!response.ok) throw new Error('Kunne ikke planlegge reise');
    return response.json();
  },

  // Lagre søk som favoritt i databasen (forutsetter at backend har POST /favorites)
  async saveFavorite(from, to) {
    
    const response = await fetch(
      `${BASE_URL}/favorites?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`,
      {
        method: 'POST'
      }
    );

    if (!response.ok) throw new Error('Kunne ikke lagre favoritt');
    return response.json();
  },
};