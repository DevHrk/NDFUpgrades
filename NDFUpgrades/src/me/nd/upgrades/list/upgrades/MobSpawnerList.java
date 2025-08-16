package me.nd.upgrades.list.upgrades;

import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.entity.MPlayer;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.ItemAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.cache.UpgradesCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.model.UpgradesModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class MobSpawnerList implements Listener {
	@EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();
        FileConfiguration m = Main.get().getConfig();
        SConfig m1 = Main.get().getConfig("Upgrades");
        SConfig m2 = Main.get().getConfig("Mensagens");
        Integration integration = Main.get().getIntegration();
        MPlayer mPlayer = MPlayer.get((Object)p);
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
        	item.getItemMeta().getDisplayName().contains(m.getString("Config.display").replace("&", "§"))) {
        	String type = ItemAPI.getInfo(e.getItemInHand(), "Entity");
        	if (!mPlayer.hasFaction()) {
            	 e.setCancelled(true);
            	 MessageUtils.send(p, m2.getString("Spawners.PlaceBlock"),m1.getStringList("Spawners.PlaceBlock"));
                 return;
             }
             m1.getSection("Upgrades").getConfigurationSection("mobs").getKeys(false).stream().filter(key ->
             UpgradesCache.get(UpgradesCache::getMobs).get(key).getType().equalsIgnoreCase(type)).forEach(key -> {
                UpgradesModel upgradesModel = UpgradesCache.get(UpgradesCache::getMobs).get(key);
            	 FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
                if (factionModel.getMobs() < upgradesModel.getLevel()) {
                    e.setCancelled(true);
                    MessageUtils.send(p, m2.getString("Spawners.Nivel").replace("{level}", String.valueOf(upgradesModel.getLevel())),m2.getStringList("Spawners.Nivel").stream().map(message -> message.replace("{level}", String.valueOf(upgradesModel.getLevel()))).collect(Collectors.toList()));
                }
            });
        }
    }
}