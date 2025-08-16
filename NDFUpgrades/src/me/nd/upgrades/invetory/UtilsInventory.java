package me.nd.upgrades.invetory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.SkullAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.CommandsUtils;
import me.nd.upgrades.utils.SimpleScroller;

public class UtilsInventory {
    
	private static final SConfig m = Main.get().getConfig("Menus", "MenuFuncoes");
	public static void open(Player p) {
		SimpleScroller scroller = new SimpleScroller(m.getString("Menu-Funcoes.nome_menu").replace("&", "§").replace("{tag}", Main.get().getIntegration().getTag(p)), m.getInt("Menu-Funcoes.tamanho"));
	    // Adiciona os itens configurados ao menu
	    Map<Integer, ItemStack> additionalItems = createItemsFromConfig("Menu-Funcoes.itens", p);
	    additionalItems.forEach((slot, item) -> {
	    	SimpleScroller.addItemToMenu(scroller, item, slot);
	        String itemSection = null;
	        for (String section : m.getSection("Menu-Funcoes.itens").getKeys(false)) {
	            if (m.getString("Menu-Funcoes.itens." + section + ".slot").equals(String.valueOf(slot))) {
	                itemSection = section;
	                break;
	            }
	        }
	        if (itemSection!= null && m.contains("Menu-Funcoes.itens." + itemSection + ".onClick")) {
	            List<String> onClickCommand = m.getStringList("Menu-Funcoes.itens." + itemSection + ".onClick");
	            SimpleScroller.addClickFunctionality(scroller, item, slot, player -> {
	            CommandsUtils.send(player, onClickCommand);
	            });
	        }
	    });
	    scroller.open(p);
	 }
		public static Map<Integer, ItemStack> createItemsFromConfig(String section,Player p) {
		    Map<Integer, ItemStack> itemMap = new HashMap<>();
		    Integration integration = Main.get().getIntegration();
		    final FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
		    for (String itemSection : m.getSection(section).getKeys(false)) {
		        String path = section + "." + itemSection + ".";
		        int slot = m.getInt(path + "slot");
		        String materialData = m.getString(path + "Material");
		        ItemStack item = Optional.of(materialData).filter(data -> data.contains(":")).map(data -> new MaterialData(Integer.parseInt(data.split(":")[0]), Byte.parseByte(data.split(":")[1])).toItemStack(1)).orElseGet(() -> new ItemStack(SkullAPI.getSkull(materialData)));
		        ItemMeta meta = item.getItemMeta();
		        meta.setDisplayName(m.getString(path + "name").replace("&", "§"));
		        List<String> lore = new ArrayList<>();
		        for (String loreLine : m.getStringList(path + "lore")) {
		            lore.add(loreLine.replace("&", "§").replace("{status}", (factionModel.getAlly() == 1 ? "§aSim" : "§cNão")));
		        }
		        meta.setLore(lore);
		        item.setItemMeta(meta);
		        itemMap.put(slot, item);
		    }
		    return itemMap;
		}
}
