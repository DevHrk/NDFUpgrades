package me.nd.upgrades.list.fragmentos;

import java.util.ArrayList;
import java.util.List;
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
import me.nd.upgrades.utils.FactionUtils;
import me.nd.upgrades.utils.MessageUtils;

public class Mining implements Listener
{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
	    SConfig m1 = Main.get().getConfig("Mensagens");
	    FileConfiguration m = Main.get().getConfig();
	    final Player p = e.getPlayer();

	    if (m.getBoolean("Fragmentos.miner.ativar") && m.getStringList("Fragmentos.miner.mundos").contains(p.getWorld().getName())) {
	        List<String> minerConfig = m.getStringList("Fragmentos.miner.type");
	        for (String config : minerConfig) {
	            String[] parts = config.split(":");
	            Material blockType = Material.valueOf(parts[0]);
	            int quantity = Integer.parseInt(parts[1]);
	            double chance = Double.parseDouble(parts[2]);

	            if (e.getBlock().getType() == blockType && FactionUtils.getUtils().getChance(chance)) {
	                final ItemStack item = new ItemStack(Material.INK_SACK, quantity);
	                final ItemMeta meta = item.getItemMeta();
	                item.setDurability((short) 11);
	                meta.setDisplayName(m.getString("Fragmento.Nome").replace("&", "§"));
	                final List<String> lore = new ArrayList<String>();
	                for (String lore122 : m.getStringList("Fragmento.Lore")) {
	                    lore.add(lore122.replace("&", "§"));
	                }
	                meta.setLore(lore);
	                item.setItemMeta(meta);
	                p.getInventory().addItem(new ItemStack[]{item});
	                MessageUtils.send(p, m1.getString("Fragmentos.achoufragmento").replace("{valor}", String.valueOf(quantity)), m1.getStringList("Fragmentos.achoufragmento").stream().map(message -> message.replace("{valor}", String.valueOf(quantity))).collect(Collectors.toList()));
	            }
	        }
	    }
	}
}
