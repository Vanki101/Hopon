package com.hopon.core.ports;

import java.util.List;

import com.hopon.core.model.FavoriteTrip;

public interface FavoriteTripRepository {

    FavoriteTrip save(FavoriteTrip trip);

    List<FavoriteTrip> findAll();

    FavoriteTrip findById(int id);

    boolean delete(int id);
}