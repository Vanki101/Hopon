package com.hopon.Core.repository;

import com.hopon.Core.ports.FavoriteRepository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryFavoriteRepository implements FavoriteRepository {

    private final List<String> favorites = new ArrayList<>();

    @Override
    public void addFavorite(String stopId) {
        favorites.add(stopId);
    }

    @Override
    public void removeFavorite(String stopId) {
        favorites.remove(stopId);
    }

    @Override
    public List<String> getFavorites() {
        return new ArrayList<>(favorites);
    }
}
