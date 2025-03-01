package net.ozaii.slimeOSCentry.managers.market.managers;

import net.ozaii.slimeOSCentry.SlimeOSCentry;
import net.ozaii.slimeOSCentry.managers.market.obj.Category;
import net.ozaii.slimeOSCentry.managers.market.obj.Product;
import net.ozaii.slimeOSCentry.playercoins.PlayerCoinsService;
import net.ozaii.slimeOSCentry.utils.ColorUtils;
import net.ozaii.slimeOSCentry.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Kategori GUI yönetimi için singleton sınıf.
 * Market arayüzünü oluşturur ve yönetir.
 */
public class CatagoryGui implements Listener {

    private static CatagoryGui instance;
    private final Inventory shopGui;
    private final CategoryManager categoryManager;
    /* products gui */
    private Inventory productGui;
    /**
     * Özel constructor. Sadece sınıf içinden veya {@link #getInstance()} metodu ile erişilebilir.
     */
    private CatagoryGui() {
        shopGui = GuiUtils.createGui(27, SlimeOSCentry.getInstance().getPrefix() + " Market");
        categoryManager = SlimeOSCentry.getInstance().getCatagoryManager();
        loadCatagorysToGui();
    }

    /**
     * CatagoryGui singleton örneğini döndürür. Eğer yoksa oluşturur.
     *
     * @return CatagoryGui örneği
     */
    public static CatagoryGui getInstance() {
        if (instance == null) {
            instance = new CatagoryGui();
        }
        return instance;
    }

    /**
     * Market GUI'sini döndürür.
     *
     * @return Market GUI'si
     */
    public Inventory getGui() {
        return shopGui;
    }

    /**
     * Market GUI'sini belirtilen oyuncuya açar.
     *
     * @param player GUI'yi açacak oyuncu
     * @throws NullPointerException player null ise
     */
    public void openShopGui(@NotNull Player player) {
        GuiUtils.openGui(player, shopGui);
    }

    /**
     * Market GUI'sini yükler ve düzenler.
     * Tüm kategorileri GUI'ye ekler.
     */
    private void loadCatagorysToGui() {
        if (categoryManager != null) {
            categoryManager.getCategories().forEach(category -> {

                ItemStack categoryItem = category.getIcon();


                ItemMeta meta = categoryItem.getItemMeta();
                meta.setDisplayName(ColorUtils.colorize(category.getName()));
                categoryItem.setItemMeta(meta);

                GuiUtils.addItemToGui(shopGui, categoryItem, category.getGuislot());
            });
        }
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == shopGui) {

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {

                /* precaution against taking  */
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void openInentory(InventoryOpenEvent event) {
        if (event.getInventory() == shopGui) {
            Player player = (Player) event.getPlayer();
           // player.sendMessage(SlimeOSCentry.getInstance().getPrefix() + " &2'ı kullandığınız için teşşekür ederiz.");
        }
    }


    private void createCategoryGui(@NotNull Player player, @NotNull Category category) {
        if (categoryManager != null) {
            /* create gui  */
            String name = ColorUtils.colorize(category.getName());

            productGui = GuiUtils.createGui(27, name);

            /* product list */
            categoryManager.getProducts().stream().forEach(product -> {
                ItemStack item = product.getIcon();
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ColorUtils.colorize(product.getName()));

                    List<String> lore = new ArrayList<>();
                    product.getDescription().stream().forEach(p -> {
                        lore.add(ColorUtils.colorize(p));
                    });

                    String price = "Fiyat: " + product.getAmount();
                    lore.add(ChatColor.GREEN + price);

                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }

                int slot = product.getGuislot();
                if (product.getCategory().contains(category.getName())) {
                    GuiUtils.addItemToGui(productGui, item, slot);
                }
            });

            /* open gui */
            GuiUtils.openGui(player, productGui);
        }
    }


    @EventHandler
    public void catagoryItemClick(@NotNull InventoryClickEvent event) {
        if (event.getInventory() == shopGui) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            int itemSlot = event.getSlot();
            if (clickedItem != null) {
              Category category = categoryManager.findCategoryFromItem(clickedItem,itemSlot);
              if (category != null) {
                  /* create category */
                  createCategoryGui(player, category);
              }
            }
        }
    }
    private static final Logger logger = SlimeOSCentry.getInstance().getLogger();
    @EventHandler
    public void productItemClick(@NotNull InventoryClickEvent event) {
        if (event.getInventory().equals(productGui)) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null) {
                logger.info("Clicked item is null.");
                return;
            }

            logger.info("Clicked item detected.");

            // Ürünü bul
            Product product = categoryManager.findProductFromItem(clickedItem);
            if (product == null) {
                logger.info("No matching product found.");
                return;
            }

            logger.info("Product found: " + product.getName());

            // Komutları al
            List<String> commands = product.getCommand();
            if (commands == null || commands.isEmpty()) {
                logger.info("No command associated with this product.");
                return;
            }

            // Asenkron coin kontrolü
            PlayerCoinsService playerCoinsService = SlimeOSCentry.getInstance().getPlayerCoinsService();
            playerCoinsService.getPlayerCoins(player.getName()).thenAcceptAsync(playerCoins -> {
                int productPrice = product.getAmount(); // Ürün fiyatı

                if (playerCoins >= productPrice) {
                    // Oyuncunun parası yeterli -> Komutu çalıştır
                    commands.forEach(command -> {
                        String finalCommand = command.replace("%player%", player.getName());
                        Bukkit.getScheduler().runTask(SlimeOSCentry.getInstance(), () -> {
                            Bukkit.dispatchCommand(player, finalCommand);
                            logger.info("Command executed: " + finalCommand);
                        });
                    });

                    // Parayı düşür
                    playerCoinsService.removePlayerCoins(player.getName(), productPrice).thenRun(() -> {
                        logger.info(player.getName() + " purchased " + product.getName() + " for " + productPrice + " coins.");
                    });
                } else {
                    player.sendMessage(ChatColor.RED + "Yetersiz bakiye! " + product.getName() + " almak için " + productPrice + " coin gerekli.");
                    logger.info(player.getName() + " does not have enough coins to purchase " + product.getName());
                }
            });

            event.setCancelled(true);
        }
    }



    /**
     * Market GUI'sini yeniden yükler.
     * Önce GUI'yi temizler, sonra kategorileri tekrar ekler.
     */
    public void reloadShopGui() {
        shopGui.clear();
        loadCatagorysToGui();
    }

}