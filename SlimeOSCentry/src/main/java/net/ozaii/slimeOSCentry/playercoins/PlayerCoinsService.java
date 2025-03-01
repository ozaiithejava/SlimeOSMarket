package net.ozaii.slimeOSCentry.playercoins;

import java.util.concurrent.CompletableFuture;

public class PlayerCoinsService {
    private final PlayerCoinsDAO playerCoinsDAO;

    public PlayerCoinsService(PlayerCoinsDAO playerCoinsDAO) {
        this.playerCoinsDAO = playerCoinsDAO;
    }

    public CompletableFuture<Void> updatePlayerCoins(String username, int newCoins) {
        PlayerCoins playerCoins = new PlayerCoins(username, newCoins);
        return playerCoinsDAO.updateCoins(playerCoins);
    }

    public CompletableFuture<Integer> getPlayerCoins(String username) {
        return playerCoinsDAO.getPlayerCoins(username)
                .thenApply(playerCoins -> playerCoins != null ? playerCoins.getCoins() : 0);
    }

    public CompletableFuture<Void> addPlayerCoins(String username, int amount) {
        return playerCoinsDAO.addCoins(username, amount);
    }

    public CompletableFuture<Void> removePlayerCoins(String username, int amount) {
        return playerCoinsDAO.removeCoins(username, amount);
    }
}
