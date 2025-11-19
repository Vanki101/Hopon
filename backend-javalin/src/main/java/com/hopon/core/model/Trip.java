package com.hopon.core.model;

import java.util.List;

public class Trip {
    private List<TripPattern> tripPatterns;

    public Trip() {}

    public Trip(List<TripPattern> tripPatterns) {
        this.tripPatterns = tripPatterns;
    }

    public List<TripPattern> getTripPatterns() {
        return tripPatterns;
    }

    public void setTripPatterns(List<TripPattern> tripPatterns) {
        this.tripPatterns = tripPatterns;
    }
}