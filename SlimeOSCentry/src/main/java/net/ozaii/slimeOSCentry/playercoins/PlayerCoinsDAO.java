package net.ozaii.slimeOSCentry.playercoins;

import net.ozaii.slimeOSCentry.managers.database.DatabaseManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class PlayerCoinsDAO {
    private final DatabaseManager databaseManager;

    public PlayerCoinsDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public CompletableFuture<Void> updateCoins(PlayerCoins playerCoins) {
        String query = "UPDATE authme SET coins = ? WHERE username = ?";
        return databaseManager.executeUpdate(query, playerCoins.getCoins(), playerCoins.getUsername());
    }

    public CompletableFuture<PlayerCoins> getPlayerCoins(String username) {
        String query = "SELECT coins FROM authme WHERE username = ?";
        return databaseManager.executeQuery(query, resultSet -> {
            try {
                if (resultSet.next()) {
                    int coins = resultSet.getInt("coins");
                    return new PlayerCoins(username, coins);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error while fetching player's coins", e);
            }
        }, username);
    }

    public CompletableFuture<Void> addCoins(String username, int amount) {
        String query = "UPDATE authme SET coins = coins + ? WHERE username = ?";
        return databaseManager.executeUpdate(query, amount, username);
    }

    public CompletableFuture<Void> removeCoins(String username, int amount) {
        String query = "UPDATE authme SET coins = GREATEST(0, coins - ?) WHERE username = ?";
        return databaseManager.executeUpdate(query, amount, username);
    }
}
