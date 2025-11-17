package com.hopon.adapter.database;

import com.hopon.core.model.FavoriteTrip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FavoriteTripRepository {

    private static final Logger log = LoggerFactory.getLogger(FavoriteTripRepository.class);
    
    private final String url;
    private final String user;
    private final String password;

    public FavoriteTripRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Lagre en ny favoritt-tur
     */
    public FavoriteTrip save(FavoriteTrip trip) {
        String sql = "INSERT INTO favorite_trips (from_location, to_location) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, trip.getFromLocation());
            stmt.setString(2, trip.getToLocation());
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    trip.setId(keys.getInt(1));
                }
            }
            
            log.info("Saved favorite trip: {} -> {}", trip.getFromLocation(), trip.getToLocation());
            return trip;
            
        } catch (SQLException e) {
            log.error("Error saving favorite trip", e);
            return null;
        }
    }

    /**
     * Hent alle favoritt-turer
     */
    public List<FavoriteTrip> findAll() {
        List<FavoriteTrip> trips = new ArrayList<>();
        String sql = "SELECT id, from_location, to_location, created_at FROM favorite_trips ORDER BY created_at DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                FavoriteTrip trip = new FavoriteTrip(
                    rs.getInt("id"),
                    rs.getString("from_location"),
                    rs.getString("to_location"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                trips.add(trip);
            }
            
            log.info("Found {} favorite trips", trips.size());
            
        } catch (SQLException e) {
            log.error("Error fetching favorite trips", e);
        }
        
        return trips;
    }

    /**
     * Slett en favoritt-tur
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM favorite_trips WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            
            log.info("Deleted favorite trip with id: {}", id);
            return rows > 0;
            
        } catch (SQLException e) {
            log.error("Error deleting favorite trip", e);
            return false;
        }
    }
}