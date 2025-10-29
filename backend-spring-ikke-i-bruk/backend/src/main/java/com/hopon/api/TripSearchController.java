package com.hopon.api;

import com.hopon.domain.Itinerary;
import com.hopon.domain.TripRequest;
import com.hopon.service.RouteSearchService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripSearchController {
    private final RouteSearchService service;

    public TripSearchController(RouteSearchService service) {
        this.service = service;
    }

    @PostMapping("/search")
    public List<Itinerary> search(@RequestBody TripRequest req) {
        return service.search(req);
    }
}
