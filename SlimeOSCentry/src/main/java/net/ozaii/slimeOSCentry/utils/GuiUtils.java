package net.ozaii.slimeOSCentry.utils;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GuiUtils {

    /**
     * Belirtilen boyut ve başlık ile bir envanter (GUI) oluşturur.
     *
     * @param size   Envanterin boyutu (9'un katı olmalı)
     * @param title  Envanter başlığı
     * @return Oluşturulan envanter
     */
    public static Inventory createGui(int size, String title) {
        return Bukkit.createInventory(null, size, title);
    }

    /**
     * GUI'ye belirtilen öğeleri ekler.
     *
     * @param inventory GUI envanteri
     * @param items     Eklenecek öğeler (slot numaralarıyla birlikte)
     */
    public static void addItemsToGui(Inventory inventory, List<GuiItem> items) {
        for (GuiItem guiItem : items) {
            if (guiItem.slot >= 0 && guiItem.slot < inventory.getSize()) {
                inventory.setItem(guiItem.slot, guiItem.item);
            }
        }
    }

    /**
     * Oyuncuya belirlenen GUI'yi açar.
     *
     * @param player    GUI'yi açacak oyuncu
     * @param inventory Açılacak envanter (GUI)
     */
    public static void openGui(Player player, Inventory inventory) {
        player.openInventory(inventory);
    }
    /**
     * GUI'ye tek bir öğe ekler.
     *
     * @param inventory GUI envanteri
     * @param item      Eklenecek öğe
     * @param slot      Öğenin ekleneceği slot
     */
    public static void addItemToGui(Inventory inventory, ItemStack item, int slot) {
        if (slot >= 0 && slot < inventory.getSize()) {
            inventory.setItem(slot, item);
        }
    }

    /**
     * Belirtilen malzeme ve özelliklerle bir öğe oluşturur.
     *
     * @param material Malzeme türü
     * @param name     Öğenin adı
     * @param lore     Öğenin açıklamaları (Lore)
     * @return Oluşturulan ItemStack
     */
    public static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * ItemsAdder kullanan item'ler için uygun bir metod ekleyebilirsin.
     * Örnek:
     * public static ItemStack createItemsAdderItem(String itemId) { ... }
     */

    /**
     * GUI içindeki bir öğeyi temsil eden yardımcı sınıf.
     */
    public static class GuiItem {
        private final ItemStack item;
        private final int slot;

        public GuiItem(ItemStack item, int slot) {
            this.item = item;
            this.slot = slot;
        }
    }
}
