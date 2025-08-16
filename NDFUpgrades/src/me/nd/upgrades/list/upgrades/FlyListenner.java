package me.nd.upgrades.list.upgrades;

import org.bukkit.event.player.*;

import me.nd.upgrades.Main;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class FlyListenner implements Listener
{
    @EventHandler
    public void onFly(final PlayerCommandPreprocessEvent e) {
    	SConfig m = Main.get().getConfig("Upgrades");
        if (e.getMessage().equalsIgnoreCase("/f voar")) {
            final Player p = e.getPlayer();
            if (!p.hasPermission(m.getString("Upgrades.Voar.upgrade1.permissao"))) {
                return;
            }
            final Integration integration = Main.get().getIntegration();
            if (!integration.hasFaction(p)) {
                return;
            }
            final FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            if (factionModel.getVoar() != 0 && !p.hasPermission(m.getString("Upgrades.Voar.upgrade1.perm"))) {
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), m.getString("Upgrades.Voar.upgrade1.comando").replace("{player}",p.getName()));
            }
        }
    }
}