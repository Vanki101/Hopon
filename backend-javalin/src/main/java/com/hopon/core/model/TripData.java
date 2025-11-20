package com.hopon.core.model;

public class TripData {
    private Trip trip;

    public TripData() {}

    public TripData(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}