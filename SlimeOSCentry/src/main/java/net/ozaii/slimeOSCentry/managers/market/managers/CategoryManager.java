package net.ozaii.slimeOSCentry.managers.market.managers;

import net.ozaii.slimeOSCentry.managers.ConfigManager;
import net.ozaii.slimeOSCentry.managers.market.obj.Category;
import net.ozaii.slimeOSCentry.managers.market.obj.Product;
import net.ozaii.slimeOSCentry.utils.GuiUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Kategori yönetimi için singleton sınıf.
 * Kategorileri ve ürünleri yükler, saklar ve bunlara erişim sağlar.
 */
public class CategoryManager {

    private static CategoryManager instance;
    private final ConfigManager configManager;
    private final List<Category> categories;
    private final List<Product> products;

    /**
     * Özel constructor. Sadece {@link #getInstance(ConfigManager)} metodu ile erişilebilir.
     *
     * @param configManager Yapılandırma yöneticisi
     * @throws NullPointerException ConfigManager null ise
     */
    private CategoryManager(ConfigManager configManager) {
        this.configManager = Objects.requireNonNull(configManager, "ConfigManager cannot be null");
        this.categories = new ArrayList<>();
        this.products = new ArrayList<>();
        this.loadCategories();
        this.loadProducts();
    }

    /**
     * CategoryManager singleton örneğini döndürür. Eğer yoksa oluşturur.
     *
     * @param configManager Yapılandırma yöneticisi
     * @return CategoryManager örneği
     * @throws NullPointerException ConfigManager null ise
     */
    public static CategoryManager getInstance(ConfigManager configManager) {
        if (instance == null) {
            instance = new CategoryManager(configManager);
        }
        return instance;
    }

    /**
     * Yapılandırma dosyasından kategorileri yükler.
     * Bu metot sadece sınıf içinde kullanılır.
     */
    private void loadCategories() {
        categories.clear();
        ConfigurationSection section = configManager.getConfig().getConfigurationSection("categories");
        if (section != null) {
            section.getKeys(false).forEach(key -> {
                String name = section.getString(key + ".name", "Unknown");
                String icon = section.getString(key + ".icon", "STONE");
                int guiSlot = section.getInt(key + ".guislot", 0);
                boolean isDecorations = section.getBoolean(key + ".is_decorations", false);

                Material material = Material.matchMaterial(icon);
                ItemStack itemIcon = (material != null) ? new ItemStack(material) : new ItemStack(Material.STONE);

                categories.add(new Category(name, itemIcon, guiSlot, isDecorations));
            });
        }
    }

    /**
     * Yapılandırma dosyasından ürünleri yükler.
     * Bu metot sadece sınıf içinde kullanılır.
     */
    private void loadProducts() {
        products.clear();

        ConfigurationSection section = configManager.getConfig().getConfigurationSection("products");

        if (section != null) {
            section.getKeys(false).forEach(key -> {
                String name = section.getString(key + ".name", "Unknown");
                String icon = section.getString(key + ".icon", "STONE");
                int amount = section.getInt(key + ".amount", 1);
                String category = section.getString(key + ".category", "Unknown");
                int guiSlot = section.getInt(key + ".guislot", 0);

                List<String> commandList = section.getStringList(key + ".command");
                List<String> descriptionList = section.getStringList(key + ".description");

                Material material = Material.matchMaterial(icon);
                if (material == null) {
                    material = Material.STONE;
                }
                ItemStack itemIcon = new ItemStack(material, amount);

                products.add(new Product(name, itemIcon, amount, descriptionList, category, guiSlot, commandList));
            });
        }
    }

    public Product findProductFromItem(ItemStack itemStack) {
        if (itemStack == null) {
            System.out.println("ItemStack is null");
            return null;
        }

        // İtem meta verisine sahip değilse null döndür
        if (!itemStack.hasItemMeta()) {
            System.out.println("ItemStack has no ItemMeta.");
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        // Display name'i kontrol et
        if (!itemMeta.hasDisplayName()) {
            System.out.println("ItemMeta has no display name.");
            return null;
        }

        // Display name'den renk kodlarını kaldır
        String itemName = ChatColor.stripColor(itemMeta.getDisplayName());
        System.out.println("Looking for item with name: " + itemName);

        // Ürünleri kontrol et ve isime göre ürünü bul
        return this.getProducts()
                .stream()
                .peek(product -> System.out.println("Checking product: " + product.getName() + " -> Stripped: " + ChatColor.stripColor(product.getName())))
                .filter(product -> {
                    String productName = ChatColor.stripColor(product.getName());
                    boolean matches = productName.equalsIgnoreCase(itemName); // Case-insensitive comparison
                    if (matches) {
                        System.out.println("Found matching product: " + product.getName());
                    }
                    return matches;
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Verilen ItemStack ve slot numarasına göre kategoriyi bulur.
     * Hem eşyanın görünümüne hem de GUI içindeki slot konumuna göre eşleştirme yapar.
     *
     * @param itemStack Kategori bulmak için kullanılacak eşya
     * @param slot      Eşyanın bulunduğu GUI slot numarası
     * @return Bulunan kategori, eğer bulunamazsa null
     * @throws NullPointerException itemStack null ise
     */
    public Category findCategoryFromItem(ItemStack itemStack, int slot) {
        if (itemStack == null) {
            throw new NullPointerException("ItemStack cannot be null");
        }

        return this.getCategories()
                .stream()
                .filter(category -> {
                    // Hem item benzerliği hem de slot numarası kontrolü
                    boolean itemMatches = category.getIcon().isSimilar(itemStack);
                    boolean slotMatches = category.getGuislot() == slot;
                    return itemMatches && slotMatches;
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Belirtilen kategori için GUI'yi oyuncuya açar.
     *
     * @param player   Oyuncu
     * @param category Kategori
     */
    public void openCategoryGui(Player player, Category category) {
        if (category == null || category.isDecorations()) {
            return;
        }

        Inventory inventory = GuiUtils.createGui(27, category.getName());

        products.stream()
                .filter(product -> product.getCategory().equals(category.getName()))
                .forEach(product -> GuiUtils.addItemToGui(inventory, product.getIcon(), product.getGuislot()));

        GuiUtils.openGui(player, inventory);
    }

    /**
     * Belirtilen kategoriye ait ürünleri döndürür.
     *
     * @param category Kategori
     * @return Kategoriye ait ürün listesi
     */
    public List<Product> getCategoryProducts(Category category) {
        return products.stream()
                .filter(product -> product.getCategory().equals(category.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Tüm kategorilerin değiştirilemez bir kopyasını döndürür.
     *
     * @return Kategori listesi
     */
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Tüm ürünlerin değiştirilemez bir kopyasını döndürür.
     *
     * @return Ürün listesi
     */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Kategori ve ürün verilerini yeniden yükler.
     */
    public void reloadData() {
        categories.clear();
        products.clear();
        loadCategories();
        loadProducts();
    }
}