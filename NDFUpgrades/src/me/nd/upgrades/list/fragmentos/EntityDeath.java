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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.nd.upgrades.Main;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class EntityDeath implements Listener
{
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
	    FileConfiguration m = Main.get().getConfig();
	    SConfig m1 = Main.get().getConfig("Mensagens");
	    double i = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
	    if (e.getEntity().getKiller() instanceof Player) {
	        if (m.getBoolean("Fragmentos.mobs.ativar") && m.getStringList("Fragmentos.mobs.mundos").contains(e.getEntity().getWorld().getName())) {
	            m.getStringList("Fragmentos.mobs.type").stream().filter(key -> e.getEntityType().getName().equalsIgnoreCase(key.split(":")[0])).forEach(key -> {
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
	                    e.getEntity().getKiller().getInventory().addItem(new ItemStack[]{item});
	                    MessageUtils.send(e.getEntity().getKiller(), m1.getString("Fragmentos.achoufragmento").replace("{valor}", key.split(":")[1]), m1.getStringList("Fragmentos.achoufragmento").stream().map(message -> message.replace("{valor}", key.split(":")[1])).collect(Collectors.toList()));
	                }
	            });
	        }
	    }
	}
}
