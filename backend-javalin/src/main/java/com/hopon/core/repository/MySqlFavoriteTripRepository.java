package com.hopon.core.repository;

import com.hopon.core.model.FavoriteTrip;
import com.hopon.core.ports.FavoriteTripRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlFavoriteTripRepository implements FavoriteTripRepository {

    private final String url;
    private final String user;
    private final String password;

    public MySqlFavoriteTripRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public FavoriteTrip save(FavoriteTrip trip) {
        String sql = "INSERT INTO favorites (from_location, to_location, created_at) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trip.getFromLocation());
            stmt.setString(2, trip.getToLocation());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();

            // Set generated ID back to the entity
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    trip.setId(keys.getInt(1));
                }
            }

            return trip;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save favorite trip", e);
        }
    }

    @Override
    public List<FavoriteTrip> findAll() {
        String sql = "SELECT id, from_location, to_location, created_at FROM favorites";
        List<FavoriteTrip> trips = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                trips.add(new FavoriteTrip(
                        rs.getInt("id"),
                        rs.getString("from_location"),
                        rs.getString("to_location"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

            return trips;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch favorite trips", e);
        }
    }

    @Override
    public FavoriteTrip findById(int id) {
        String sql = "SELECT id, from_location, to_location, created_at FROM favorites WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Favorites(
                            rs.getInt("id"),
                            rs.getString("from_location"),
                            rs.getString("to_location"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find favorite trip by id", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM favorites WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete favorite trip", e);
        }
    }
}
