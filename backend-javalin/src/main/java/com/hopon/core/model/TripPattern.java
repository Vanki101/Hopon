package com.hopon.core.model;

import java.util.List;

public class TripPattern {
    private String expectedStartTime;
    private String expectedEndTime;
    private int duration;
    private double walkDistance;
    private List<Leg> legs;

    public TripPattern() {}

    public TripPattern(String expectedStartTime, String expectedEndTime, int duration, 
                      double walkDistance, List<Leg> legs) {
        this.expectedStartTime = expectedStartTime;
        this.expectedEndTime = expectedEndTime;
        this.duration = duration;
        this.walkDistance = walkDistance;
        this.legs = legs;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getWalkDistance() {
        return walkDistance;
    }

    public void setWalkDistance(double walkDistance) {
        this.walkDistance = walkDistance;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
}