package com.hopon;

import com.hopon.domain.*;
import com.hopon.provider.RouteProvider;
import com.hopon.service.RouteSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

public class RouteSearchServiceTests {
  @Test
  void returnsMockItinerary() {
    // Arrange
    RouteProvider provider = Mockito.mock(RouteProvider.class);
    List<Itinerary> mock = List.of(
      new Itinerary(List.of(new Leg(new Place("A","From"),
                                    new Place("C","To"),
                                    Mode.BUS, 10)), 10)
    );
    Mockito.when(provider.find(Mockito.any())).thenReturn(mock);

    // Act
    var service = new RouteSearchService(provider);
    var res = service.search(new TripRequest("From","To","2025-09-25T08:00:00Z"));

    // Assert
    Assertions.assertFalse(res.isEmpty());
    Assertions.assertEquals(10, res.get(0).totalMinutes());
  }
}
