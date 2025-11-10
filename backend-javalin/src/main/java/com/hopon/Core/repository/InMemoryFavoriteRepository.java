package com.hopon.core.repository;

import com.hopon.core.ports.FavoriteRepository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryFavoriteRepository implements FavoriteRepository {

    private final List<String> favorites = new ArrayList<>();

    @Override
    public List<String> findAll() {
        return new ArrayList<>(favorites);
    }

    @Override
    public void add(String stopId) {
        if (!favorites.contains(stopId)) {
            favorites.add(stopId);
        }
    }

    @Override
    public void remove(String stopId) {
        favorites.remove(stopId);
    }
}
