package me.nd.upgrades;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

import me.nd.upgrades.api.APIS;
import me.nd.upgrades.api.ItemAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.command.Commands;
import me.nd.upgrades.dao.FactionDAO;
import me.nd.upgrades.database.SQLConnector;
import me.nd.upgrades.database.SQLExecutor;
import me.nd.upgrades.database.SQLProvider;
import me.nd.upgrades.database.SQLite;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.integration.Mamba;
import me.nd.upgrades.listener.Listeners;
import me.nd.upgrades.minecraft.version.GetVersions;
import me.nd.upgrades.minecraft.version.Version;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.FileUtils;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.FactionUtils;
import me.nd.upgrades.utils.ReflectionUtils;

public class Main extends JavaPlugin {
	
    public void onEnable() {
    	ReflectionUtils.loadUtils();
    	GetVersions.version = Version.getServerVersion();
	    Main.get().setIntegration((Integration)new Mamba());
    	APIS.load();
        Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] Plugin iniciado");
    	saveDefaultConfig();
    	fileUtils = new FileUtils(this);
        Commands.setupCommands();
        Listeners.setupListeners();
        ItemAPI.load();
        sqlConnector = SQLProvider.of(Main.get()).setup();
        sqlExecutor = new SQLExecutor(sqlConnector);
        FactionUtils.getUtils().loadUpgrades();
        factionDAO = new FactionDAO(sqlExecutor);
        SQLite.openConnection();
        factionDAO.getAllFactions().forEach(key -> FactionCache.get(FactionCache::getCache).put(key.getTag(), key));
      }
    
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§b[NDFUpgrades] §cPlugin Desligado");
	    Map<String, FactionModel> cache = FactionCache.getInstance().getCache();
	    for (FactionModel faction : cache.values()) {
	        SQLite.saveFaction(faction);
	    }
    	((HashMap)FactionCache.get(FactionCache::getCache)).keySet().forEach(key -> this.factionDAO.saveAll((FactionModel)((HashMap)FactionCache.get(FactionCache::getCache)).get(key)));
    	SQLite.closeConnection();
	}    
	
      public Integration getIntegration() {
        return this.integration;
      }
      public FactionDAO getFactionDAO() {
          return this.factionDAO;
      }

      public SQLConnector getSqlConnector() {
          return this.sqlConnector;
      }

      public SQLExecutor getSqlExecutor() {
          return this.sqlExecutor;
      }

      public void setIntegration(Integration integration) {
          this.integration = integration;
      }
      public FileUtils getFileUtils() {
          return this.fileUtils;
        }
      
      public SConfig getConfig(String name) {
          return this.getConfig("", name);
        }
        public SConfig getConfig(String path, String name) {
          return SConfig.getConfig(this, "plugins/" + this.getName() + "/" + path, name);
        }
        
    	public static Main get() {
            return (Main)JavaPlugin.getPlugin((Class)Main.class);
          } 
        
    public SQLConnector sqlConnector;
    private FactionDAO factionDAO;
    public SQLExecutor sqlExecutor;
	private Integration integration;
	private FileUtils fileUtils;
}