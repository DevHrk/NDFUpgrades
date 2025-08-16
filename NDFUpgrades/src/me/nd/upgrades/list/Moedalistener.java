package me.nd.upgrades.list;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.entity.MPlayer;

import org.bukkit.configuration.file.FileConfiguration;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.FormatterAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class Moedalistener implements Listener
{
	
	@EventHandler
	void colocar(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		SConfig m1 = Main.get().getConfig("Mensagens");
		FileConfiguration m = Main.get().getConfig();
        	if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
               	 && item.getItemMeta().getDisplayName().equals(m.getString("Moeda.Nome").replace("&", "§"))) {
               	  MessageUtils.send(p, m1.getString("Moeda.Colocar"),m1.getStringList("Moeda.Colocar"));
               	  e.setCancelled(true);
               	return;
                 }
        }
	
	@EventHandler
	void PlayerInteract(final PlayerInteractEvent e) {
	    FileConfiguration m = Main.get().getConfig();
	    SConfig m1 = Main.get().getConfig("Mensagens");
	    Player p = e.getPlayer();
	    final MPlayer p2 = MPlayer.get((Object)p);
	    ItemStack item = p.getItemInHand();
	    Integration integration = Main.get().getIntegration();

	    if (!p2.hasFaction()) {
	        if (e.getAction()!= Action.LEFT_CLICK_AIR && e.getAction()!= Action.LEFT_CLICK_BLOCK) {
	            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
	                    && item.getItemMeta().getDisplayName().equals(m.getString("Moeda.Nome").replace("&", "§"))) {
	                MessageUtils.send(p, m1.getString("Moeda.Utilizar"),m1.getStringList("Moeda.Utilizar"));
	                return;
	            }
	        }
	    }
	    new BukkitRunnable() {
	        @Override
	        public void run() {
	    if (p.isSneaking()) {
	        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
	                && item.getItemMeta().getDisplayName().equals(m.getString("Moeda.Nome").replace("&", "§"))) {
	            int amount = 0;
	            for (ItemStack i : p.getInventory().getContents()) {
	                if (i!= null && i.hasItemMeta() && i.getItemMeta().hasDisplayName() 
	                        && i.getItemMeta().getDisplayName().equals(m.getString("Moeda.Nome").replace("&", "§"))) {
	                    amount += i.getAmount();
	                }
	            }
	            ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).setMoedas(((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getMoedas() + amount * m.getInt("Moeda.Preço"));
	            String message = m1.getString("Moeda.depositoumoeda").replace("{amount}", String.valueOf(FormatterAPI.formatter(amount)));
	            List<String> messageList = new ArrayList<>();
	            for (String line : m1.getStringList("Moeda.depositoumoeda")) {
	                messageList.add(line.replace("{amount}", String.valueOf(FormatterAPI.formatter(amount))));
	            }
	            MessageUtils.send(p, message, messageList);
	            removeItem(p, item);
	            return;
	        }
	    }
	            if (e.getAction()!= Action.LEFT_CLICK_AIR && e.getAction()!= Action.LEFT_CLICK_BLOCK) {
	                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
	                        && item.getItemMeta().getDisplayName().equals(m.getString("Moeda.Nome").replace("&", "§"))) {
	                    ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).setMoedas(((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getMoedas() + m.getInt("Moeda.Preço"));
	                    String message = m1.getString("Moeda.depositoumoeda").replace("{amount}", "1");
	                    List<String> messageList = new ArrayList<>();
	                    for (String line : m1.getStringList("Moeda.depositoumoeda")) {
	                        messageList.add(line.replace("{amount}", "1"));
	                    }
	                    MessageUtils.send(p, message, messageList);
	                    removeItem(p);
	                }
	            }
	        }
	    }.runTaskLater(Main.get(), 1L);
	}

	private void removeItem(Player p, ItemStack item) {
	    p.getInventory().removeItem(item);
	    for (ItemStack i : p.getInventory().getContents()) {
	        if (i != null && i.hasItemMeta() && i.getItemMeta().hasDisplayName() 
	                && i.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
	            p.getInventory().removeItem(i);
	        }
	    }
	}
	
	private void removeItem(Player p) {
	    if (p.getItemInHand().getAmount() < 2) {
	        p.setItemInHand(new ItemStack(Material.AIR));
	    } else {
	        ItemStack item = p.getItemInHand();
	        item.setAmount(item.getAmount() - 1);
	    }
	}
	
}
