package com.hopon.core.model;

import java.time.LocalDateTime;

public class FavoriteTrip {
    private int id;
    private String fromLocation;
    private String toLocation;
    private LocalDateTime createdAt;

    public FavoriteTrip() {}

    public FavoriteTrip(int id, String fromLocation, String toLocation, LocalDateTime createdAt) {
        this.id = id;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.createdAt = createdAt;
    }

    public FavoriteTrip(String fromLocation, String toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}