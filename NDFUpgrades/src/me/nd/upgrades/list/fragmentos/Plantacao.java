package me.nd.upgrades.list.fragmentos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.nd.upgrades.Main;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class Plantacao implements Listener{
    
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e) {
	    SConfig m1 = Main.get().getConfig("Mensagens");
	    FileConfiguration m = Main.get().getConfig();
	    final Player p = e.getPlayer();

	    double i = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
	    if (m.getBoolean("Fragmentos.plantacoes.ativar") && m.getStringList("Fragmentos.plantacoes.mundos").contains(e.getBlock().getWorld().getName())) {
	        m.getStringList("Fragmentos.plantacoes.type").stream().filter(key -> e.getBlock().getType() == Material.getMaterial(key.split(":")[0])).forEach(key -> {
	            ItemStack item = new ItemStack(Material.INK_SACK, Integer.parseInt(key.split(":")[1]));
	            double percentage = Double.parseDouble(key.split(":")[2]);
	            if (i <= percentage) {
	                ItemMeta meta = item.getItemMeta();
	                item.setDurability((short) 11);
	                meta.setDisplayName(m.getString("Fragmento.Nome").replace("&", "§"));
	                List<String> lore = new ArrayList<>();
	                for (String lore122 : m.getStringList("Fragmento.Lore")) {
	                    lore.add(lore122.replace("&", "§"));
	                }
	                meta.setLore(lore);
	                item.setItemMeta(meta);
	                p.getInventory().addItem(new ItemStack[]{item});
	                MessageUtils.send(p, m1.getString("Fragmentos.achoufragmento").replace("{valor}", String.valueOf(Integer.parseInt(key.split(":")[1]))), m1.getStringList("Fragmentos.achoufragmento").stream().map(message -> message.replace("{valor}", String.valueOf(Integer.parseInt(key.split(":")[1])))).collect(Collectors.toList()));
	            }
	        });
	    }
	}
	
}
