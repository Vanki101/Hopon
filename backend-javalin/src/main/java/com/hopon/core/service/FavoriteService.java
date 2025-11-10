package com.hopon.core.service;

import com.hopon.core.ports.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FavoriteService {

    private static final Logger log = LoggerFactory.getLogger(FavoriteService.class);

    private final FavoriteRepository repository;

    public FavoriteService(FavoriteRepository repository) {
        this.repository = repository;
    }

    public List<String> getAll() {
        log.info("Retrieving all favorites");
        try {
            var favorites = repository.findAll();
            log.debug("Found {} favorites", favorites.size());
            return favorites;
        } catch (Exception e) {
            log.error("Error retrieving favorites: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public void add(String stopId) {
        log.info("Adding favorite stopId={}", stopId);
        try {
            repository.add(stopId);
            log.debug("Favorite added successfully: {}", stopId);
        } catch (Exception e) {
            log.error("Error adding favorite stopId={}: {}", stopId, e.getMessage(), e);
        }
    }

    public void remove(String stopId) {
        log.info("Removing favorite stopId={}", stopId);
        try {
            repository.remove(stopId);
            log.debug("Favorite removed successfully: {}", stopId);
        } catch (Exception e) {
            log.error("Error removing favorite stopId={}: {}", stopId, e.getMessage(), e);
        }
    }
}
