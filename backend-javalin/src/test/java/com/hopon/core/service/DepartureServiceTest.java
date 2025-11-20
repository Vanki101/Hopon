package com.hopon.core.service;

import com.hopon.adapter.entur.MockEnturClient;
import com.hopon.core.model.Departure;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.hopon.core.service.DepartureService;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepartureServiceTest {
    @Test
    void testEmptyDepartures() {
        MockEnturClient mockClient = new MockEnturClient();
        com.hopon.core.service.DepartureService service = new DepartureService(mockClient);

        List<Departure> result = service.getDepartures("123");
        assertFalse(result.isEmpty(), "Departures should not be empty");
    }
}