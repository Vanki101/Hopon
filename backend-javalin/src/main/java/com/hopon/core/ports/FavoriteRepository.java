package com.hopon.Core.ports;

import java.util.List;

public interface FavoriteRepository {
    void addFavorite(String stopId);
    void removeFavorite(String stopId);
    List<String> getFavorites();
}
