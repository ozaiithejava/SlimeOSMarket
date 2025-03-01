package net.ozaii.slimeOSCentry.playercoins;

public class PlayerCoins {
    private String username;
    private int coins;

    public PlayerCoins(String username, int coins) {
        this.username = username;
        this.coins = coins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
