package com.hopon.core.model;

public class Leg {
    private String mode;
    private double distance;
    private int duration;
    private String expectedStartTime;
    private String expectedEndTime;
    private Line line;
    private Place fromPlace;
    private Place toPlace;

    public Leg() {}

    public Leg(String mode, double distance, int duration, String expectedStartTime,
              String expectedEndTime, Line line, Place fromPlace, Place toPlace) {
        this.mode = mode;
        this.distance = distance;
        this.duration = duration;
        this.expectedStartTime = expectedStartTime;
        this.expectedEndTime = expectedEndTime;
        this.line = line;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getExpectedStartTime() {
        return expectedStartTime;
    }

    public void setExpectedStartTime(String expectedStartTime) {
        this.expectedStartTime = expectedStartTime;
    }

    public String getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(String expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Place getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(Place fromPlace) {
        this.fromPlace = fromPlace;
    }

    public Place getToPlace() {
        return toPlace;
    }

    public void setToPlace(Place toPlace) {
        this.toPlace = toPlace;
    }
}