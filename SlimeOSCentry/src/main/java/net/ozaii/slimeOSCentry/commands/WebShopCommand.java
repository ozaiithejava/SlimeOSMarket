package net.ozaii.slimeOSCentry.commands;

import net.ozaii.slimeOSCentry.SlimeOSCentry;
import net.ozaii.slimeOSCentry.managers.market.managers.CatagoryGui;
import net.ozaii.slimeOSCentry.managers.market.managers.CategoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WebShopCommand implements CommandExecutor {

    private final CategoryManager categoryManager;
    private final CatagoryGui catagoryGui;

    public WebShopCommand() {
        this.categoryManager = SlimeOSCentry.getInstance().getCatagoryManager();
        this.catagoryGui = SlimeOSCentry.getInstance().getGui();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(SlimeOSCentry.getInstance().getPrefix() +" Bu komutu sadece oyuncular kullanabilir.");
            return false;
        }

        Player player = (Player) sender;
        if (player.hasPermission("slimeoscentry.shop") || player.isOp()){
           catagoryGui.openShopGui(player);
        }

        return true;
    }
}
