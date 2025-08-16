package me.nd.upgrades.database;

import org.bukkit.plugin.java.*;

import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.*;
import java.io.*;

public class SQLProvider
{
    private final JavaPlugin plugin;
    
    public SQLConnector setup() {
        final FileConfiguration config = this.plugin.getConfig();
        SQLConnector sqlConnector;
            final ConfigurationSection sqlite = config.getConfigurationSection("SQLite");
            sqlConnector = this.sqliteDatabaseType(sqlite).connect();
            Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] [SQLite]§f conexão com o SQLite efetuada com sucesso, criando tabelas...");
            return sqlConnector;
    }
 
    private SQLDatabaseType sqliteDatabaseType(ConfigurationSection section) {
        return SQLiteDatabaseType.builder().file(new File(this.plugin.getDataFolder(), section.getString("file"))).build();
    }
    
    private SQLProvider(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public static SQLProvider of(final JavaPlugin plugin) {
        return new SQLProvider(plugin);
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SQLProvider)) {
            return false;
        }
        final SQLProvider other = (SQLProvider)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$plugin = this.getPlugin();
        final Object other$plugin = other.getPlugin();
        if (this$plugin == null) {
            if (other$plugin == null) {
                return true;
            }
        }
        else if (this$plugin.equals(other$plugin)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SQLProvider;
    }
    
    @SuppressWarnings("unused")
	@Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $plugin = this.getPlugin();
        result = result * 59 + (($plugin == null) ? 43 : $plugin.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SQLProvider(plugin=" + this.getPlugin() + ")";
    }
}
