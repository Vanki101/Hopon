package com.hopon.domain;
import java.util.List;
public record Itinerary(List<Leg> legs, int totalMinutes) {}
