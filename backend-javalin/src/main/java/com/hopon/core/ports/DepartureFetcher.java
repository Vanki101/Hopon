package com.hopon.Core.ports;

import com.hopon.Core.model.Departure;
import java.util.List;

public interface DepartureFetcher {
    List<Departure> fetchDepartures(String stopId);
}
