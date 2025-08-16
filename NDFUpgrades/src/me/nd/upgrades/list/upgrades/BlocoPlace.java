package me.nd.upgrades.list.upgrades;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.massivecraft.factions.entity.MPlayer;

import me.nd.upgrades.Main;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.cache.UpgradesCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.model.UpgradesModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class BlocoPlace implements Listener {
	@EventHandler
    public void BlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        SConfig m = Main.get().getConfig("Upgrades");
        SConfig m1 = Main.get().getConfig("Mensagens");
        Integration integration = Main.get().getIntegration();
        MPlayer mPlayer = MPlayer.get((Object)p);
        for (String item12 : m.getSection("Upgrades.blocos").getKeys(false)) {
        if (e.getBlock().getType() == Material.getMaterial(m.getString("Upgrades.blocos." + item12 + ".type"))) {
            if (!mPlayer.hasFaction()) {
              	 e.setCancelled(true);
              	 MessageUtils.send(p, m1.getString("Blocos.PlaceBlock"),m1.getStringList("Blocos.PlaceBlock"));
                 return;
            
        }
           ConfigurationSection configurationSection = m.getSection("Upgrades");
            configurationSection.getConfigurationSection("blocos").getKeys(false).stream().filter(key ->
             UpgradesCache.get(UpgradesCache::getBlocos).get(key).getType().equalsIgnoreCase
              (m.getString("Upgrades.blocos." + item12 + ".type"))).forEach(key -> {
                UpgradesModel upgradesModel = UpgradesCache.get(UpgradesCache::getBlocos).get(key);
                 FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
                 if (factionModel.getBlocos() < upgradesModel.getLevel()) {
                  e.setCancelled(true);
                  MessageUtils.send(p, m1.getString("Blocos.Nivel").replace("{level}", String.valueOf(upgradesModel.getLevel())),m1.getStringList("Blocos.PlaceBlock").stream().map(message -> message.replace("{level}", String.valueOf(upgradesModel.getLevel()))).collect(Collectors.toList()));
                }         
            });
         }
        }
    }
}	