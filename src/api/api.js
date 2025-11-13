javascriptconst BASE_URL = 'http://localhost:8080';

export const api = {
  // Health check
  async healthCheck() {
    const response = await fetch(${BASE_URL}/health);
    return response.text();
  },

  // Søk etter stoppesteder
  async searchStops(name) {
    const response = await fetch(${BASE_URL}/search?name=${encodeURIComponent(name)});
    if (!response.ok) throw new Error('Søk feilet');
    return response.json();
  },

  // Hent avganger
  async getDepartures(nameOrId) {
    const param = nameOrId.startsWith('NSR:') ? id=${nameOrId} : name=${encodeURIComponent(nameOrId)};
    const response = await fetch(${BASE_URL}/departures?${param});
    if (!response.ok) throw new Error('Kunne ikke hente avganger');
    return response.json();
  },

  // Planlegg reise
  async planTrip(from, to) {
    const response = await fetch(
      ${BASE_URL}trip?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}
    );
    if (!response.ok) throw new Error('Kunne ikke planlegge reise');
    return response.json();
  }

  if (!response.ok) {
    const msg = await response.text();
    throw new Error(`Feil ${response.status}: ${msg}`);
  }
  
};