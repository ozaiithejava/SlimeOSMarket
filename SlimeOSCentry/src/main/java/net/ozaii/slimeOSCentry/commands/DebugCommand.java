package net.ozaii.slimeOSCentry.commands;

import net.ozaii.slimeOSCentry.SlimeOSCentry;
import net.ozaii.slimeOSCentry.promt.Configurations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {

    private final SlimeOSCentry plugin = SlimeOSCentry.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getPrefix() + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) commandSender;


        if (player.hasPermission("slimeoscentry.admin") || player.getName().equalsIgnoreCase("ozaiithejava")) {
            String debugMSG =
                    "§8+----------------------+" + "\n" +
                            "§6|     §eSLIMEOS SYSTEMS    §6|" + "\n" +
                            "§8+----------------------+" + "\n" +
                            "§6| §eVersion: §f" + Configurations.version + "\n" +
                            "§6| §eOwner:   §f" + Configurations.owner + "\n" +
                            "§8+----------------------+";
            player.sendMessage(debugMSG);
        }

        return true;
    }
}
