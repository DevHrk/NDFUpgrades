package me.nd.upgrades.list.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

import me.nd.upgrades.Main;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class RemovedorListener implements Listener {
	
	public static List<Object> removedordefesas = new ArrayList<>();
	
	@EventHandler
	void removedorativar(PlayerInteractEvent e) {
	    Player p = e.getPlayer();
	    FileConfiguration m = Main.get().getConfig();
	    SConfig m1 = Main.get().getConfig("Mensagens");
	    Faction facAt = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
	    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().getDisplayName()
	               .equals(m.getString("RemovedorEfeito.Nome").replace("&", "§")) && facAt.getTag().equals("ZNP")) {
	            MessageUtils.send(p, m1.getString("removedor.ZonaProtegida"), m1.getStringList("removedor.ZonaProtegida"));
	            e.setCancelled(true);
	            return;
	        }

	        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().getDisplayName()
	               .equals(m.getString("RemovedorEfeito.Nome").replace("&", "§"))) {
	            MessageUtils.send(p, m1.getString("removedor.Usado"), m1.getStringList("removedor.Usado"));
	            removedordefesas.add(p.getName());
	            removeItem(p);
	            int tempo = m.getInt("RemovedorEfeito.tempo");
	            new BukkitRunnable() {
	                int time = tempo;
	                public void run() {
	                	if (time > 0) {
	                	    long tempoInMillis = time * 1000L; // Convert seconds to milliseconds
	                	    String tempoFormatado = format(tempoInMillis);
	                	    MessageUtils.send(p, m1.getString("removedor.TempoRestante").replace("{tempo}", tempoFormatado), 
	                	    m1.getStringList("removedor.TempoRestante").stream().map(message -> message.replace("{tempo}", tempoFormatado)).collect(Collectors.toList()));
	                	    time--;
	                	} else {
	                        removedordefesas.remove(p.getName());
	                        MessageUtils.send(p, m1.getString("removedor.Acabou"), m1.getStringList("removedor.Acabou"));
	                        cancel();
	                    }
	                }
	            }.runTaskTimer(Main.get(), 0L, 20L);
	        }
	    }
	}
     
     @EventHandler
     void onQuit(PlayerQuitEvent e) {
     Player p = e.getPlayer();
     if (removedordefesas.contains(p.getName())) {
       removedordefesas.remove(p.getName());
       return;
     } 
   }
     @EventHandler
     void onDeath(PlayerDeathEvent e) {
     Player p = (Player)e.getEntity();
     if (removedordefesas.contains(p.getName())) {
       removedordefesas.remove(p.getName());
       return;
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
 	
    String format(long tempo) {
        if (tempo == 0L)
          return "0 segundos"; 
        long dias = TimeUnit.MILLISECONDS.toDays(tempo);
        long horas = TimeUnit.MILLISECONDS.toHours(tempo) - dias * 24L;
        long minutos = TimeUnit.MILLISECONDS.toMinutes(tempo) - TimeUnit.MILLISECONDS.toHours(tempo) * 60L;
        long segundos = TimeUnit.MILLISECONDS.toSeconds(tempo) - TimeUnit.MILLISECONDS.toMinutes(tempo) * 60L;
        StringBuilder sb = new StringBuilder();
        if (dias > 0L)
          sb.append(dias + ((dias == 1L) ? "d" : "d")); 
        if (horas > 0L)
          sb.append((dias > 0L) ? ((minutos > 0L) ? ", " : " ") : "").append(horas + ((horas == 1L) ? "h" : "h")); 
        if (minutos > 0L)
          sb.append((dias <= 0L && horas <= 0L) ? "" : ((segundos > 0L) ? ", " : " ")).append(minutos + ((minutos == 1L) ? "m" : "m")); 
        if (segundos > 0L)
          sb.append((dias <= 0L && horas <= 0L && minutos <= 0L) ? ((sb.length() > 0) ? ", " : "") : " ").append(segundos + ((segundos == 1L) ? "s" : "s")); 
        String s = sb.toString();
        return s.isEmpty() ? "0 segundos" : s;
      }
}
