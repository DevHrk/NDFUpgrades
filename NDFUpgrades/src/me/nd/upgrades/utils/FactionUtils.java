package me.nd.upgrades.utils;

import org.bukkit.configuration.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import me.nd.upgrades.Main;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.cache.UpgradesCache;
import me.nd.upgrades.dao.FactionDAO;
import me.nd.upgrades.database.SQLite;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.model.UpgradesModel;
import me.nd.upgrades.plugin.SConfig;

public class FactionUtils
{
    private static final FactionUtils utils = new FactionUtils();
    private final Random random = new Random();

    public void loadUpgrades() {
    	SConfig m1 = Main.get().getConfig("Upgrades");
        ConfigurationSection configurationSection = m1.getSection("Upgrades");;
        configurationSection.getConfigurationSection("poder").getKeys(false).forEach(key -> {
            UpgradesModel upgradesModel = UpgradesModel.builder().title(configurationSection.getString("poder." + key + ".title").replace("&", "§")).level(Integer.valueOf(configurationSection.getInt("poder." + key + ".level"))).price(Double.valueOf(configurationSection.getDouble("poder." + key + ".price"))).build();
                    ((HashMap<String, UpgradesModel>)UpgradesCache.get(UpgradesCache::getPoder)).put(key, upgradesModel);
                  });
              configurationSection.getConfigurationSection("membros").getKeys(false).forEach(key -> {
                    UpgradesModel upgradesModel = UpgradesModel.builder().title(configurationSection.getString("membros." + key + ".title").replace("&", "§")).level(Integer.valueOf(configurationSection.getInt("membros." + key + ".level"))).price(Double.valueOf(configurationSection.getDouble("membros." + key + ".price"))).build();
                    ((HashMap<String, UpgradesModel>)UpgradesCache.get(UpgradesCache::getMembros)).put(key, upgradesModel);
                  });
              configurationSection.getConfigurationSection("mobs").getKeys(false).forEach(key -> {
            	  UpgradesModel upgradesModel = UpgradesModel.builder().title(configurationSection.getString("mobs." + key + ".title").replace("&", "§")).level(Integer.valueOf(configurationSection.getInt("mobs." + key + ".level"))).price(Double.valueOf(configurationSection.getDouble("mobs." + key + ".price"))).type(configurationSection.getString("mobs." + key + ".type")).build();
            	  ((HashMap<String, UpgradesModel>)UpgradesCache.get(UpgradesCache::getMobs)).put(key, upgradesModel);
              });
              configurationSection.getConfigurationSection("blocos").getKeys(false).forEach(key -> {
            	  UpgradesModel upgradesModel = UpgradesModel.builder().title(configurationSection.getString("blocos." + key + ".title").replace("&", "§")).level(Integer.valueOf(configurationSection.getInt("blocos." + key + ".level"))).price(Double.valueOf(configurationSection.getDouble("blocos." + key + ".price"))).type(configurationSection.getString("blocos." + key + ".type")).build();
            	  ((HashMap<String, UpgradesModel>)UpgradesCache.get(UpgradesCache::getBlocos)).put(key, upgradesModel);
              });
              configurationSection.getConfigurationSection("attack").getKeys(false).forEach(key -> {
            	  UpgradesModel upgradesModel = UpgradesModel.builder().title(configurationSection.getString("attack." + key + ".title").replace("&", "§")).level(Integer.valueOf(configurationSection.getInt("attack." + key + ".level"))).price(Double.valueOf(configurationSection.getDouble("attack." + key + ".price"))).time(configurationSection.getInt("attack." + key + ".time")).build();
            	  ((HashMap<String, UpgradesModel>)UpgradesCache.get(UpgradesCache::getAttack)).put(key, upgradesModel);
              });
              configurationSection.getConfigurationSection("defesas").getKeys(false).forEach(key -> {
            	  UpgradesModel upgradesModel = UpgradesModel.builder().title(configurationSection.getString("defesas." + key + ".title").replace("&", "§")).level(Integer.valueOf(configurationSection.getInt("defesas." + key + ".level"))).price(Double.valueOf(configurationSection.getDouble("defesas." + key + ".price"))).time(configurationSection.getInt("defesas." + key + ".time")).type(configurationSection.getString("defesas." + key + ".type")).build();
            	  ((HashMap<String, UpgradesModel>)UpgradesCache.get(UpgradesCache::getDefesas)).put(key, upgradesModel);
              });
    }
    
	
	public void removeFactionToCache(String tag) {
	    FactionModel factionModel = FactionModel.builder().tag(tag).poder(0).membros(0).mobs(0).moedas(0).voar(0).plantacao(0).blocos(0).attack(0).defesas(0).ally(0).build();
	    FactionDAO factionDAO = new FactionDAO(SQLite.getExecutor());
	    factionDAO.createFaction(factionModel);
	    ((HashMap<String, FactionModel>)FactionCache.get(FactionCache::getCache)).put(tag, factionModel);
	}
	
	
	public void saveAllFactionsToDB() {
	    FactionDAO factionDAO = new FactionDAO(SQLite.getExecutor());
	    FactionCache cache = FactionCache.getInstance();
	    cache.getCache().values().forEach(factionDAO::saveAll);
	}

	public void addFactionToCache(String tag) {
	    FactionCache cache = FactionCache.getInstance();
	    if (!cache.getCache().containsKey(tag)) {
	        FactionModel factionModel = FactionModel.builder()
	                .tag(tag)
	                .build(); // Initialize with default values
	        cache.getCache().put(tag, factionModel);
	        if (!factionExistsInDB(tag)) {
	            saveFactionToDB(factionModel);
	        }
	    }
	}
	
	public void addFactionToCach(String tag) {
        if (!((HashMap)FactionCache.get(FactionCache::getCache)).containsKey(tag)) {
            FactionModel factionModel = FactionModel.builder().tag(tag).poder(Integer.valueOf(0)).membros(Integer.valueOf(0)).mobs(Integer.valueOf(0)).moedas(Integer.valueOf(0)).voar(Integer.valueOf(0)).plantacao(Integer.valueOf(0)).blocos(Integer.valueOf(0)).attack(Integer.valueOf(0)).defesas(Integer.valueOf(0)).ally(Integer.valueOf(0)).build();
            ((HashMap<String, FactionModel>)FactionCache.get(FactionCache::getCache)).put(tag, factionModel);
            SQLite.createFacInDB(factionModel);
        }
    }

	public boolean factionExistsInDB(String tag) {
	    try (PreparedStatement preparedStatement = SQLite.getConnection().prepareStatement("SELECT 1 FROM fupgrade WHERE tag =?")) {
	        preparedStatement.setString(1, tag);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            return resultSet.next();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public void saveFactionToDB(FactionModel factionModel) {
	    SQLite.saveFaction(factionModel);
	}


    public boolean hasItem(Player p, ItemStack item, int amount) {
        boolean val = false;
        for (ItemStack inv : p.getInventory().getContents()) {
          if (inv != null && 
            p.getInventory().containsAtLeast(item, amount) && 
            inv.isSimilar(item)) {
            val = true;
            break;
          } 
        } 
        return val;
      }

    public boolean getChance(double percent) {
        if (percent < 0.0 || percent > 100.0) {
            throw new IllegalArgumentException("A porcentagem nao pode ser maior do que 100 nem menor do que 0");
        }
        double result = this.random.nextDouble() * 100.0;
        return result <= percent;
    }

    public static FactionUtils getUtils() {
        return utils;
    }
}
