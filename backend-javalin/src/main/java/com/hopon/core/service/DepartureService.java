package com.hopon.Core.service;

import com.hopon.Core.model.Departure;
import com.hopon.Core.ports.DepartureFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DepartureService {

    //  Logger alltid rett under class-deklarasjonen
    private static final Logger log = LoggerFactory.getLogger(DepartureService.class);

    private final DepartureFetcher departureFetcher;

    public DepartureService(DepartureFetcher departureFetcher) {
        this.departureFetcher = departureFetcher;
    }

    public List<Departure> getDepartures(String stopId) {
        log.info("Fetching departures for stopId={}", stopId);

        try {
            var departures = departureFetcher.fetchDepartures(stopId);

            if (departures == null || departures.isEmpty()) {
                log.warn("No departures found for stopId={}", stopId);
            } else {
                log.debug("Fetched {} departures for stopId={}", departures.size(), stopId);
            }

            return departures;
        } catch (Exception e) {
            log.error("Error retrieving departures for stopId={}: {}", stopId, e.getMessage(), e);
            return List.of();
        }
    }
}
