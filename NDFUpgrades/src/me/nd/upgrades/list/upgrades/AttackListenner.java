package me.nd.upgrades.list.upgrades;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.engine.EngineFly;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.event.EventFactionsEnteredInAttack;
import com.massivecraft.massivecore.ps.PS;

import me.nd.upgrades.Main;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;

import java.util.Set;

public class AttackListenner implements Listener {
	
	@EventHandler
	public void attack(EventFactionsEnteredInAttack e) {
	    SConfig m12 = Main.get().getConfig("Upgrades");
	    final FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(e.getFaction().getTag());
	    int attackLevel = factionModel.getAttack();
	    int attackTime = m12.getInt("Upgrades.attack.upgrade" + attackLevel + ".time");
	    if (attackLevel > 0) {
	        new BukkitRunnable() {
	            @Override
	            public void run() {
	                e.getFaction().setInAttack(true);
	                EngineSobAtaque.get();
	                EngineSobAtaque.facs.add(e.getFaction());
	                EngineFly.disableFlyFaction(e.getFaction());
	                Set<PS> chunks = BoardColl.get().getChunks(e.getFaction());
	                for (PS ps : chunks) {
	                    EngineSobAtaque.get();
	                    EngineSobAtaque.underattack.put(ps.asBukkitChunk(), System.currentTimeMillis() + attackTime * 1000);
	                }
	            }
	        }.runTaskLaterAsynchronously(Main.get(), 2L);
	    }
	}
}
