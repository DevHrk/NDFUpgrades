package me.nd.upgrades.list.upgrades;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import me.nd.upgrades.Main;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.list.item.RemovedorListener;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;

public class Defesas implements Listener {
	
	@EventHandler
	public void defesas(PlayerMoveEvent e) {
	    Player p = e.getPlayer();
	    MPlayer p2 = MPlayer.get(p);
	    Faction facAt = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
	    SConfig m1 = Main.get().getConfig("Upgrades");
	    
	    FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(facAt.getTag());
	    
	    if (facAt.isNone() || facAt.getTag().equals("ZNL") || facAt.getTag().equals("ZNP") || facAt.getTag().equals("ZNG")) {
	        return;
	    }
	    
	    if (p2.getFactionTag().equals(facAt.getTag()) || (factionModel.getAlly() == 1 && facAt.getAllys().contains(p2.getFaction()))) {
	        return;
	    }
	    
	    if (RemovedorListener.removedordefesas.contains(p.getName())) {
	        return;
	    }
	    
	    int defesasLevel = factionModel.getDefesas();
	    if (defesasLevel > 0) {
	        for (int i = 1; i <= defesasLevel; i++) {
	            String type = m1.getString("Upgrades.defesas.upgrade" + i + ".type");
	            int time = m1.getInt("Upgrades.defesas.upgrade" + i + ".time");
	            p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(type), 100, time - 1));
	        }
	    }
	}

}
