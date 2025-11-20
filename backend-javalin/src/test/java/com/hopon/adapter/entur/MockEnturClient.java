package com.hopon.adapter.entur;

import com.hopon.core.model.Departure;
import com.hopon.core.ports.DepartureFetcher;

import java.time.LocalDateTime;
import java.util.List;

// Enkel mock som returnerer testdata
public class MockEnturClient implements DepartureFetcher {
    @Override
    public List<Departure> fetchDepartures(String stopPlaceId) {
        return List.of(new Departure("Mock", "Test", "1", LocalDateTime.now(), "buss"));
    }
}
