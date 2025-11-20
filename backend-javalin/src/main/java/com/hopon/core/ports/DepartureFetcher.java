package com.hopon.core.ports;

import com.hopon.core.model.Departure;
import java.util.List;

public interface DepartureFetcher {
    List<Departure> fetchDepartures(String stopId);
}
