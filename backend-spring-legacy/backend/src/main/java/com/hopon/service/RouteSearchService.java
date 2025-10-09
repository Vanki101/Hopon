package com.hopon.service;

import com.hopon.domain.Itinerary;
import com.hopon.domain.TripRequest;
import com.hopon.provider.RouteProvider;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteSearchService {
    private final RouteProvider provider;

    public RouteSearchService(RouteProvider provider) {
        this.provider = provider;
    }

    public List<Itinerary> search(TripRequest req) {
        return provider.find(req);
    }
}
