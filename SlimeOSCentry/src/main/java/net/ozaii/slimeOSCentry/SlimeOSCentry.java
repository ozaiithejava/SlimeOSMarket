package net.ozaii.slimeOSCentry;

import net.ozaii.slimeOSCentry.commands.DebugCommand;
import net.ozaii.slimeOSCentry.commands.WebShopCommand;
import net.ozaii.slimeOSCentry.managers.ConfigManager;
import net.ozaii.slimeOSCentry.managers.database.DatabaseConfig;
import net.ozaii.slimeOSCentry.managers.database.DatabaseManager;
import net.ozaii.slimeOSCentry.managers.market.managers.CatagoryGui;
import net.ozaii.slimeOSCentry.managers.market.managers.CategoryManager;
import net.ozaii.slimeOSCentry.managers.web.LisanceControlManager;
import net.ozaii.slimeOSCentry.managers.web.VersionControlManager;
import net.ozaii.slimeOSCentry.placeholder.SlimeOSPlaceholderExpansion;
import net.ozaii.slimeOSCentry.playercoins.PlayerCoinsDAO;
import net.ozaii.slimeOSCentry.playercoins.PlayerCoinsService;
import net.ozaii.slimeOSCentry.promt.Configurations;
import net.ozaii.slimeOSCentry.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * SlimeOSCentry ana sınıfı.
 * Web market ve oyuncu paraları için bir Minecraft eklentisi.
 */
public final class SlimeOSCentry extends JavaPlugin {

    private static SlimeOSCentry instance;
    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private PlayerCoinsDAO playerCoinsDAO;
    private PlayerCoinsService playerCoinsService;
    private CategoryManager categoryManager;
    private CatagoryGui categoryGui;
    private Logger logger;
    private String prefix;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        logger.info(Configurations.slimeOS);

        /* lisance checker */
        new LisanceControlManager().checkLisance();
        /* version checker */
        new VersionControlManager().checkVersion(Configurations.version);
        configManager = ConfigManager.getInstance(this);
        configManager.setupConfig();

        prefix = configManager.getString("prefix");

        if (!performStartupChecks() || !initializeDatabase()) {
            disablePlugin("Eklenti devre dışı bırakıldı.");
            return;
        }

        initializeServices();
        initializeManagers();
        registerCommands();
        registerListeners();
        registerPlaceHolders();

        logger.info("SlimeOSCentry başarıyla etkinleştirildi!");
    }

    /**
     * Yönetici sınıflarını başlatır.
     */
    private void initializeManagers() {
        categoryManager = CategoryManager.getInstance(configManager);
        categoryManager =CategoryManager.getInstance(configManager);
        logger.info("Toplamda " + categoryManager.getCategories().size() + " kadar katagori yüklendi.");
        logger.info("Toplamda " + categoryManager.getProducts().size() + " kadar ürün yüklendi." );
        logger.info("sitemarket gui si yükleniyor...");
        categoryGui = CatagoryGui.getInstance();
        logger.info("sitemarket gui si yüklendi.");
    }

    /**
     * Komutları kaydeder.
     */
    private void registerCommands() {
        getCommand("sitemarket").setExecutor(new WebShopCommand());
        getCommand("debug_slimeos").setExecutor(new DebugCommand());
    }

    /**
     * Event dinleyicilerini kaydeder.
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(categoryGui, this);
    }

    /**
     * Başlangıç kontrollerini gerçekleştirir.
     *
     * @return Kontrollerden geçildi mi?
     */
    private boolean performStartupChecks() {
        if (configManager == null) {
            logger.severe("ConfigManager oluşturulamadı!");
            return false;
        }

        if (!configManager.getBoolean("setup.state")) {
            logger.warning("Sunucu kurulumu yapılmadı! Lütfen 'setup.state' değerini true yaparak server'ı reload edin.");
            return false;
        }

        return true;
    }

    /**
     * Veritabanı bağlantısını başlatır.
     *
     * @return Veritabanı başarıyla başlatıldı mı?
     */
    private boolean initializeDatabase() {
        String dbName = configManager.getString("database.db-name");
        int port = configManager.getInt("database.port");
        String username = configManager.getString("database.username");
        String password = configManager.getString("database.password");
        String jdbcUrl = "jdbc:mysql://localhost:" + port + "/" + dbName;

        DatabaseConfig config = new DatabaseConfig(jdbcUrl, username, password);
        databaseManager = DatabaseManager.getInstance(config);

        if (databaseManager == null) {
            logger.severe("Database manager oluşturulamadı!");
            return false;
        }

        return true;
    }

    /**
     * Servisleri başlatır.
     */
    private void initializeServices() {
        playerCoinsDAO = new PlayerCoinsDAO(databaseManager);
        playerCoinsService = new PlayerCoinsService(playerCoinsDAO);
        if (playerCoinsDAO == null){
            logger.warning("Coin Dao null.");
        }
        if (playerCoinsService == null){
            logger.warning("Coin Service null.");
        }
        logger.info("Tüm servisler başarıyla başlatıldı.");
    }

    /**
     * Plugin'i devre dışı bırakır.
     *
     * @param reason Devre dışı bırakma nedeni
     */
    private void disablePlugin(String reason) {
        logger.warning(reason);
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
            logger.info("Veritabanı bağlantısı kapatıldı.");
        }
        logger.info("SlimeOSCentry devre dışı bırakıldı.");
        instance = null;
        databaseManager = null;
        configManager = null;
    }

    /**
     * Plugin singleton örneğini döndürür.
     *
     * @return Plugin örneği
     * @throws IllegalStateException Plugin henüz başlatılmadıysa
     */
    public static SlimeOSCentry getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin henüz başlatılmadı!");
        }

        return instance;
    }

    private static void registerPlaceHolders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SlimeOSPlaceholderExpansion(instance).register();
        } else {
            instance.getLogger().warning("PlaceholderAPI bulunamadı! Placeholder'lar çalışmayacak.");
        }
    }


    // Getters
    /**
     * DatabaseManager örneğini döndürür.
     *
     * @return DatabaseManager örneği
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * ConfigManager örneğini döndürür.
     *
     * @return ConfigManager örneği
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * PlayerCoinsDAO örneğini döndürür.
     *
     * @return PlayerCoinsDAO örneği
     */
    public PlayerCoinsDAO getPlayerCoinsDAO() {
        return playerCoinsDAO;
    }

    /**
     * PlayerCoinsService örneğini döndürür.
     *
     * @return PlayerCoinsService örneği
     */
    public PlayerCoinsService getPlayerCoinsService() {
        return playerCoinsService;
    }

    /**
     * CategoryManager örneğini döndürür.
     *
     * @return CategoryManager örneği
     */
    public CategoryManager getCatagoryManager() {
        return categoryManager;
    }

    /**
     * CatagoryGui örneğini döndürür.
     *
     * @return CatagoryGui örneği
     */
    public CatagoryGui getGui() {
        return categoryGui;
    }

    /**
     * Plugin ön ekini döndürür.
     *
     * @return Plugin ön eki
     */
    public String getPrefix() {
        return  ColorUtils.colorize(prefix);
    }
}