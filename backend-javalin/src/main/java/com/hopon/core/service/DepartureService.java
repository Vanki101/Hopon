package com.hopon.Core.service;

import com.hopon.Core.model.Departure;
import com.hopon.Core.ports.DepartureFetcher;
import java.util.List;

public class DepartureService {
    private final DepartureFetcher fetcher;

    public DepartureService(DepartureFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public List<Departure> getDepartures(String stopId) {
        if (stopId == null || stopId.isBlank()) {
            throw new IllegalArgumentException("Stop ID cannot be empty");
        }
        return fetcher.fetchDepartures(stopId);
    }
}
