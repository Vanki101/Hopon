package com.hopon.core.service;

import com.hopon.core.model.Departure;
import com.hopon.core.ports.DepartureFetcher;
import com.hopon.adapter.entur.EnturClient;
import com.hopon.adapter.entur.EnturLocationClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DepartureService {

    private static final Logger log = LoggerFactory.getLogger(DepartureService.class);

    private final DepartureFetcher departureFetcher;
    private final EnturLocationClient locationClient;

    public DepartureService(DepartureFetcher departureFetcher) {
        this.departureFetcher = departureFetcher;
        this.locationClient = new EnturLocationClient();
    }

    public List<Departure> getDepartures(String stopNameOrId) {
        log.info("Fetching departures for input={}", stopNameOrId);

        try {
            String stopId = stopNameOrId;

            if (!stopNameOrId.startsWith("NSR:StopPlace:")) {
                log.info("Looking up StopPlace ID for name='{}' via geocoder", stopNameOrId);
                String lookedUpId = locationClient.findFirstStopPlaceId(stopNameOrId);

                if (lookedUpId == null) {
                    log.warn("No StopPlace ID found for '{}'", stopNameOrId);
                    return List.of();
                }

                stopId = lookedUpId;
                log.info("Resolved '{}' → '{}'", stopNameOrId, stopId);
            }

            var departures = departureFetcher.fetchDepartures(stopId);

            if (departures == null || departures.isEmpty()) {
                log.warn("No departures found for stopId={}", stopId);
            }

            return departures;

        } catch (Exception e) {
            log.error("Error retrieving departures for '{}': {}", stopNameOrId, e.getMessage(), e);
            return List.of();
        }
    }
}
