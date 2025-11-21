package com.hopon.adapter.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hopon.core.model.FavoriteTrip;
import com.hopon.core.ports.FavoriteTripRepository;

public class FavoriteTripRepositorySqlite implements FavoriteTripRepository {
    // Vi trenger ikke lenger url/user/password siden de er håndtert i Database-klassen
    public FavoriteTripRepositorySqlite() {
    }

    @Override
    public FavoriteTrip save(FavoriteTrip trip) {
        String sql = "INSERT INTO favorites (from_location, to_location, created_at) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trip.getFromLocation());
            stmt.setString(2, trip.getToLocation());
            stmt.setString(3, LocalDateTime.now().toString());

            stmt.executeUpdate();

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

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                trips.add(new FavoriteTrip(
                        rs.getInt("id"),
                        rs.getString("from_location"),
                        rs.getString("to_location"),
                        LocalDateTime.parse(rs.getString("created_at"))
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

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FavoriteTrip(
                            rs.getInt("id"),
                            rs.getString("from_location"),
                            rs.getString("to_location"),
                            LocalDateTime.parse(rs.getString("created_at"))
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

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete favorite trip", e);
        }
    }
}