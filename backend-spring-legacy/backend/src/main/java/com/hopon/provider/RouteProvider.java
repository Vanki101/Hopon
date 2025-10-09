package com.hopon.provider;

import com.hopon.domain.Itinerary;
import com.hopon.domain.TripRequest;
import java.util.List;

public interface RouteProvider {
  List<Itinerary> find(TripRequest request);
}
