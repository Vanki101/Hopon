package com.hopon.Core.service;

import com.hopon.Core.ports.FavoriteRepository;
import java.util.List;

public class FavoriteService {
    private final FavoriteRepository repo;

    public FavoriteService(FavoriteRepository repo) {
        this.repo = repo;
    }

    public void add(String stopId) {
        if (stopId == null || stopId.isBlank()) {
            throw new IllegalArgumentException("Stop ID cannot be empty");
        }
        repo.addFavorite(stopId);
    }

    public void remove(String stopId) {
        repo.removeFavorite(stopId);
    }

    public List<String> getAll() {
        return repo.getFavorites();
    }
}
