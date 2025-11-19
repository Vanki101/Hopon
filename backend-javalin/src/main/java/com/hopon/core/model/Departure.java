package com.hopon.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Departure {

    private final String from;
    private final String to;
    private final String line;
    private final LocalDateTime departureTime;
    private final String transportMode;

    public Departure(String from, String to, String line, LocalDateTime departureTime, String transportMode) {
        this.from = from;
        this.to = to;
        this.line = line;
        this.departureTime = departureTime;
        this.transportMode = transportMode;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getLine() {
        return line;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    @JsonProperty("transportMode")  
    public String getTransportMode() {
        return transportMode;
    }

    public String getTime() {
        return departureTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getDate() {
        return departureTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
