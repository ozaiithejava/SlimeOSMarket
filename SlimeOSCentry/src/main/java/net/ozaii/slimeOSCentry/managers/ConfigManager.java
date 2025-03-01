package net.ozaii.slimeOSCentry.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {

    private static ConfigManager instance;
    private final Plugin plugin;
    private FileConfiguration config;
    private File configFile;

    // Private constructor to ensure Singleton pattern
    private ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    // Static method to get the singleton instance
    public static ConfigManager getInstance(Plugin plugin) {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager(plugin);
                }
            }
        }
        return instance;
    }

    public void setupConfig() {
        this.plugin.saveDefaultConfig();
        if (!this.plugin.getDataFolder().exists())
            this.plugin.getDataFolder().mkdir();
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!this.configFile.exists())
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        saveConfig();
    }

    public World getWorld(String worldName) {
        return Bukkit.getWorld(worldName);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }
    public <T> java.util.List<T> getList(String path) {
        return (List<T>) this.config.getList(path);
    }
    // Konfigürasyondan ItemStack almak
    public ItemStack getItemStack(String path) {
        String itemName = config.getString(path + ".name");
        String itemType = config.getString(path + ".type");
        int amount = config.getInt(path + ".amount");

        ItemStack itemStack = new ItemStack(Material.matchMaterial(itemType), amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(itemName);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
