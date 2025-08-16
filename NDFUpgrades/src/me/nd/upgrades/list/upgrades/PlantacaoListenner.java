package me.nd.upgrades.list.upgrades;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import me.nd.upgrades.Main;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.ItemBuilder;
import me.nd.upgrades.utils.MessageUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

public class PlantacaoListenner implements Listener {

	@EventHandler
     void onBreakNW(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        SConfig m1 = Main.get().getConfig("Mensagens");
        SConfig m2 = Main.get().getConfig("Upgrades");
        Integration integration = Main.get().getIntegration();
        if (!integration.hasFaction(p).booleanValue()) {
            return;
        }
        
        FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
        if (factionModel.getPlantacao() != 0) {
            MPlayer player = MPlayer.get((Object)p);
            BlockState state = e.getBlock().getState();
            MaterialData data = state.getData();
            FileConfiguration config = Main.get().getConfig();
            if (data instanceof NetherWarts) {
                e.setCancelled(true);
                if (!BoardColl.get().getFactionAt(PS.valueOf((Location)p.getLocation())).equals(player.getFaction())) {
                    MessageUtils.send(p, m1.getString("Replantar.Fungo"),m1.getStringList("Replantar.Fungo"));
                    e.setCancelled(true);
                    return;
                }
                NetherWarts netherWarts = (NetherWarts)data;
                if (netherWarts.getState() == NetherWartsState.RIPE) {
                    boolean cancel = true;
                    for (ItemStack items : p.getInventory().getContents()) {
                        if (items == null || !items.getType().name().equals("NETHER_STALK")) continue;
                        cancel = false;
                        netherWarts.setState(NetherWartsState.SEEDED);
                        state.setData((MaterialData)netherWarts);
                        state.update();
                    }
                    if (cancel) {
                    	MessageUtils.send(p, m1.getString("Replantar.FungoInv"),m1.getStringList("Replantar.FungoInv"));
                    	e.setCancelled(true);
                    	return;
                    }else {
                        double i = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
                        for (String value : m2.getStringList("Upgrades.Plantacoes.Fungos")) {
                        	double percentage = Double.parseDouble(value.split(":")[0]);
                            String command = value.split(":")[1];
                            String moeda = value.split(":")[1];
                            String fragmentos = value.split(":")[1];
                            List<String> lore = config.getStringList("Moeda.Lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                            List<String> lore1 = config.getStringList("Fragmento.Lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                           
                            if (!(i <= percentage)) continue;
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replace("{player}", p.getName()));
                            if(moeda.equalsIgnoreCase("{moeda}")) {
                            String mensagem = value.split(":")[3];
                            p.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DOUBLE_PLANT, Integer.parseInt(value.split(":")[2])).setDurability((short)11).setName(config.getString("Moeda.Nome").replace("&", "§")).setLore(lore).toItemStack() });
                            p.sendMessage(mensagem.replace("&", "§").replace("{valor}", String.valueOf(Integer.parseInt(value.split(":")[2]))));
                            }
                            if(fragmentos.equalsIgnoreCase("{fragmentos}")) {
                            String mensagem = value.split(":")[3];
                            p.getInventory().addItem(new ItemStack[] {new ItemBuilder(Material.INK_SACK, Integer.parseInt(value.split(":")[2])).setDurability((short)11).setName(config.getString("Fragmento.Nome").replace("&", "§")).setLore(lore1).toItemStack() });
                            p.sendMessage(mensagem.replace("&", "§").replace("{valor}", String.valueOf(Integer.parseInt(value.split(":")[2]))));    
                            }
                 
                        }
                        
                        removeItems(p.getInventory(), Material.NETHER_STALK, 1);
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.0, 1.0, 0.0), new ItemStack(Material.NETHER_STALK, 3));
                        p.updateInventory();
                        }
                        
                }
            }
        }
    }
	
	 public int removeItems(Inventory inventory, Material type, int amount) {
	       
	        if(type == null || inventory == null)
	            return -1;       
	        if (amount <= 0)
	            return -1;
	       
	        if (amount == Integer.MAX_VALUE) {
	            inventory.remove(type);
	            return 0;
	        }
	       
	        inventory.removeItem(new ItemStack(type,amount));
	        return 0;
	    }
}
