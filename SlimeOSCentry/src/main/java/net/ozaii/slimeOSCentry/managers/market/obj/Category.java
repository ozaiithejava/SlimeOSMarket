package net.ozaii.slimeOSCentry.managers.market.obj;

import org.bukkit.inventory.ItemStack;

public class Category {

    private String name;
    private ItemStack icon;
    private int guislot;
    private boolean is_decorations;

    public Category(String name, ItemStack icon, int guislot, boolean is_decorations) {
        this.name = name;
        this.icon = icon;
        this.guislot = guislot;
        this.is_decorations = is_decorations;
    }

    /* getters */
    public String getName() { return name; }
    public ItemStack getIcon() { return icon; }
    public int getGuislot() { return guislot; }
    public boolean isDecorations() { return is_decorations; }

    /* setters */
    public void setName(String name) { this.name = name; }
    public void setIcon(ItemStack icon) { this.icon = icon; }
    public void setGuislot(int guislot) { this.guislot = guislot; }
    public void setIs_decorations(boolean is_decorations) { this.is_decorations = is_decorations; }
}
