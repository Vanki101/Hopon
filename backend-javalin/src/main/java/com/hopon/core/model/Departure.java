package com.hopon.Core.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Departure {
    private String from;
    private String to;
    private String line;
    private LocalDateTime departureTime;

    public Departure(String from, String to, String line, LocalDateTime departureTime) {
        this.from = from;
        this.to = to;
        this.line = line;
        this.departureTime = departureTime;
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

    // Disse to gir deg pent formatert dato og tid i JSON
    public String getDate() {
        return departureTime.toLocalDate().toString();
    }

    public String getTime() {
        return departureTime.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
