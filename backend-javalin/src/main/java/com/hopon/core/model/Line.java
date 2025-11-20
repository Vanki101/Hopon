package com.hopon.core.model;

public class Line {
    private String id;
    private String publicCode;
    private String transportMode;

    public Line() {}

    public Line(String id, String publicCode, String transportMode) {
        this.id = id;
        this.publicCode = publicCode;
        this.transportMode = transportMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicCode() {
        return publicCode;
    }

    public void setPublicCode(String publicCode) {
        this.publicCode = publicCode;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }
}