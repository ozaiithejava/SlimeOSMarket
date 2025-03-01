package net.ozaii.slimeOSCentry.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.ozaii.slimeOSCentry.SlimeOSCentry;
import net.ozaii.slimeOSCentry.promt.Configurations;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SlimeOSPlaceholderExpansion extends PlaceholderExpansion {

    private final Plugin plugin;


    public SlimeOSPlaceholderExpansion(Plugin plugin) {
        this.plugin = plugin;

    }

    @Override
    public @NotNull String getIdentifier() {
        return "SlimeOS";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ozaii1337";
    }

    @Override
    public @NotNull String getVersion() {
        return Configurations.version;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) return "";

        switch (identifier) {
            case "coin":
               return SlimeOSCentry.getInstance().getPlayerCoinsService().getPlayerCoins(player.getName()).join() + "";
            case "version":
                return "Ver: " + Configurations.version;
            case "owner":
                return Configurations.owner + "";
            default:
                return null;
        }
    }
}
