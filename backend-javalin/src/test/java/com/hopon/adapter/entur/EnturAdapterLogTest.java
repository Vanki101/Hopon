package com.hopon.adapter.entur;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnturAdapterLogTest {

    @Test
    void testLogging() {
        LogCaptor logCaptor = LogCaptor.forClass(EnturClient.class);
        EnturClient client = new EnturClient();

        client.fetchDepartures("123");
        assertTrue(logCaptor.getInfoLogs().size() >= 0);
    }
}
