package com.hopon.core.model;

public class TripResponse {
    private TripData data;

    public TripResponse() {}

    public TripResponse(TripData data) {
        this.data = data;
    }

    public TripData getData() {
        return data;
    }

    public void setData(TripData data) {
        this.data = data;
    }
}