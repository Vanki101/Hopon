package com.hopon.core.ports;

import java.util.List;

public interface FavoriteRepository {

    // Henter alle favoritter
    List<String> findAll();

    // Legger til en ny favoritt
    void add(String stopId);

    // Fjerner en favoritt
    void remove(String stopId);
}
