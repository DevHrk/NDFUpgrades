package me.nd.upgrades.api;

import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import java.util.*;

public class ConfigAPI
{
    private Plugin plugin;
    private String name;
    private File file;
    private FileConfiguration config;
    
    public String getName() {
        return this.name;
    }
    
    public void setPlugin(final Plugin plugin) {
        this.plugin = plugin;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
	public ConfigAPI(final String name, final Plugin plugin) {
        this.plugin = plugin;
        if (plugin == null) {
            this.plugin = (Plugin)JavaPlugin.getProvidingPlugin((Class)this.getClass());
        }
        this.name = name;
        this.reloadConfig();
    }
    
    public ConfigAPI(final String name) {
        this(name, null);
    }
    
	public ConfigAPI reloadConfig() {
        this.file = new File(this.plugin.getDataFolder(), this.name);
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        final InputStream defaults = this.plugin.getResource(this.file.getName());
        if (defaults != null) {
            final YamlConfiguration loadConfig = YamlConfiguration.loadConfiguration(defaults);
            this.config.setDefaults((Configuration)loadConfig);
        }
        return this;
    }
    
    public ConfigAPI saveConfig() {
        try {
            this.config.save(this.file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }
    
    public String message(final String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path));
    }
    
    public List<String> getMessages(final String path) {
        final List<String> messages = new ArrayList<String>();
        for (final String line : this.getStringList(path)) {
            messages.add(toChatMessage(line));
        }
        return messages;
    }
    
    public void saveDefaultConfig() {
        if (this.plugin.getResource(this.name) != null) {
            this.plugin.saveResource(this.name, false);
        }
        this.reloadConfig();
    }
    
    public void saveResource() {
        this.plugin.saveResource(this.name, true);
    }
    
    public void remove(final String path) {
        this.config.set(path, (Object)null);
    }
    
    public ConfigAPI saveDefault() {
        this.config.options().copyDefaults(true);
        this.saveConfig();
        return this;
    }
    
    public ItemStack getItem(final String path) {
        return (ItemStack)this.get(path);
    }
    
    public Location getLocation(final String path) {
        return (Location)this.get(path);
    }
    
    public static String toChatMessage(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public static String toConfigMessage(final String text) {
        return text.replace("\u0e22\u0e07", "&");
    }
    
    public boolean delete() {
        return this.file.delete();
    }
    
    public boolean exists() {
        return this.file.exists();
    }
    
    public void add(final String path, final Object value) {
        this.config.addDefault(path, value);
    }
    
    public boolean contains(final String path) {
        return this.config.contains(path);
    }
    
    public ConfigurationSection create(final String path) {
        return this.config.createSection(path);
    }
    
    public Object get(final String path) {
        return this.config.get(path);
    }
    
    public boolean getBoolean(final String path) {
        return this.config.getBoolean(path);
    }
    
    public ConfigurationSection getSection(final String path) {
        if (!this.config.isConfigurationSection(path)) {
            this.config.createSection(path);
        }
        return this.config.getConfigurationSection(path);
    }
    
    public double getDouble(final String path) {
        return this.config.getDouble(path);
    }
    
    public int getInt(final String path) {
        return this.config.getInt(path);
    }
    
    public List<Integer> getIntegerList(final String path) {
        return (List<Integer>)this.config.getIntegerList(path);
    }
    
    public ItemStack getItemStack(final String path) {
        return this.config.getItemStack(path);
    }
    
    public Set<String> getKeys(final boolean deep) {
        return (Set<String>)this.config.getKeys(deep);
    }
    
    public List<?> getList(final String path) {
        return (List<?>)this.config.getList(path);
    }
    
    public long getLong(final String path) {
        return this.config.getLong(path);
    }
    
    public List<Long> getLongList(final String path) {
        return (List<Long>)this.config.getLongList(path);
    }
    
    public List<Map<?, ?>> getMapList(final String path) {
        return (List<Map<?, ?>>)this.config.getMapList(path);
    }
    
    public String getString(final String path) {
        return this.config.getString(path);
    }
    
    public List<String> getStringList(final String path) {
        return (List<String>)this.config.getStringList(path);
    }
    
    public Map<String, Object> getValues(final boolean deep) {
        return (Map<String, Object>)this.config.getValues(deep);
    }
    
    public void set(final String path, final Object value) {
        this.config.set(path, value);
    }
}
