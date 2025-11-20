package com.hopon.adapter.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // DB-filen vil ligge i mappa der du kjører "mvn exec:java"
    private static final String DB_URL = "jdbc:sqlite:hopon.db";

    static {
        try {
            // Last inn SQLite-driveren
            Class.forName("org.sqlite.JDBC");
            // Opprett tabeller hvis de ikke finnes
            initSchema();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Kunne ikke laste SQLite-driveren", e);
        } catch (SQLException e) {
            throw new RuntimeException("Feil ved opprettelse av database/tabeller", e);
        }
    }

    private static void initSchema() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Tilpass til deres eksisterende felter i favorites-tabellen
            String sql = """
                CREATE TABLE IF NOT EXISTS favorites (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    from_location TEXT NOT NULL,
                    to_location   TEXT NOT NULL,
                    created_at    TEXT NOT NULL
                );
                """;

            stmt.execute(sql);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}