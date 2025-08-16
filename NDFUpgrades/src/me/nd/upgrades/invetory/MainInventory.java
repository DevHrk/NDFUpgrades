package me.nd.upgrades.invetory;

import org.bukkit.entity.*;
import java.util.*;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.FormatterAPI;
import me.nd.upgrades.api.SkullAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.CommandsUtils;
import me.nd.upgrades.utils.SimpleScroller;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.Banner;

public class MainInventory {
	
	private static final SConfig m = Main.get().getConfig("Menus", "MenuPrincipal");
	public static void openPrincipal(Player p) {
		SimpleScroller scroller = new SimpleScroller(m.getString("Menu.principal.nome_menu").replace("&", "§").replace("{tag}", Main.get().getIntegration().getTag(p)), m.getInt("Menu.principal.tamanho"));
	    // Adiciona os itens configurados ao menu
	    Map<Integer, ItemStack> additionalItems = createItemsFromConfig("Menu.principal.itens", p);
	    additionalItems.forEach((slot, item) -> {
	    	SimpleScroller.addItemToMenu(scroller, item, slot);
	        String itemSection = null;
	        for (String section : m.getSection("Menu.principal.itens").getKeys(false)) {
	            if (m.getString("Menu.principal.itens." + section + ".slot").equals(String.valueOf(slot))) {
	                itemSection = section;
	                break;
	            }
	        }
	        if (itemSection!= null && m.contains("Menu.principal.itens." + itemSection + ".onClick")) {
	            List<String> onClickCommand = m.getStringList("Menu.principal.itens." + itemSection + ".onClick");
	            SimpleScroller.addClickFunctionality(scroller, item, slot, player -> {
	            CommandsUtils.send(player, onClickCommand);
	            });
	        }
	    });
	    scroller.open(p);
	 }
	
	private static final SConfig m1 = Main.get().getConfig("Menus", "MenuUpgrades");
	public static void openUpgrades(Player p) {
		SimpleScroller scroller = new SimpleScroller(m1.getString("menu-upgrade.nome_menu").replace("&", "§").replace("{tag}", Main.get().getIntegration().getTag(p)), m1.getInt("menu-upgrade.tamanho"));
	    // Adiciona os itens configurados ao menu
	    Map<Integer, ItemStack> additionalItems = createItemsFromConfig1("menu-upgrade.itens", p);
	    additionalItems.forEach((slot, item) -> {
	    	SimpleScroller.addItemToMenu(scroller, item, slot);
	        String itemSection = null;
	        for (String section : m1.getSection("menu-upgrade.itens").getKeys(false)) {
	            if (m1.getString("menu-upgrade.itens." + section + ".slot").equals(String.valueOf(slot))) {
	                itemSection = section;
	                break;
	            }
	        }
	        if (itemSection!= null && m1.contains("menu-upgrade.itens." + itemSection + ".onClick")) {
	            List<String> onClickCommand = m1.getStringList("menu-upgrade.itens." + itemSection + ".onClick");
	            SimpleScroller.addClickFunctionality(scroller, item, slot, player -> {
	            CommandsUtils.send(player, onClickCommand);
	            });
	        }
	    });
	    scroller.open(p);
	 }
	
	public static Map<Integer, ItemStack> createItemsFromConfig1(String section,Player p) {
	    Map<Integer, ItemStack> itemMap = new HashMap<>();
	    SConfig m2 = Main.get().getConfig("Upgrades");
	    Integration integration = Main.get().getIntegration();
	    final MPlayer p2 = MPlayer.get((Object)p);
	    FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
	    FactionModel factionModela = null;
	    HashMap<String, FactionModel> cache = (HashMap) FactionCache.get(FactionCache::getCache);
	    if (cache != null) {
	        factionModela = cache.get(integration.getTag(p));
	    } 
	    for (String itemSection : m1.getSection(section).getKeys(false)) {
	        String path = section + "." + itemSection + ".";
	        int slot = m1.getInt(path + "slot");
	        String materialData = m1.getString(path + "Material");
	        ItemStack item = Optional.of(materialData).filter(data -> data.contains(":")).map(data -> new MaterialData(Integer.parseInt(data.split(":")[0]), Byte.parseByte(data.split(":")[1])).toItemStack(1)).orElseGet(() -> new ItemStack(SkullAPI.getSkull(materialData)));
	        ItemMeta meta = item.getItemMeta();
	        meta.setDisplayName(m1.getString(path + "name").replace("&", "§"));
	        List<String> lore = new ArrayList<>();
	        for (String loreLine : m1.getStringList(path + "lore")) {
	            lore.add(loreLine.replace("&", "§").replace("{fac}", p2.getFactionTag())
		    			.replace("{moedas}", FormatterAPI.formatter(factionModela != null ? factionModela.getMoedas() : 0))
		    			.replace("{statusvoar}", factionModel.getVoar() > Integer.valueOf(m.getInt("Upgrades.Voar.upgrade1.level")) ? "§aSua facção ja possui esse upgrade." : ("§7Custo: §f" + FormatterAPI.formatter(m2.getInt("Upgrades.Voar.upgrade1.price")) + " Moedas"))
		    			.replace("{statusplantacao}", factionModel.getPlantacao() > Integer.valueOf(m.getInt("Upgrades.Plantacoes.upgrade1.level")) ? "§aSua facção ja possui esse upgrade." : ("§7Custo: §f" + FormatterAPI.formatter(m2.getInt("Upgrades.Plantacoes.upgrade1.price")) + " Moedas")));
	        }
	        meta.setLore(lore);
	        item.setItemMeta(meta);
	        itemMap.put(slot, item);
	    }
	    return itemMap;
	}

	public static Map<Integer, ItemStack> createItemsFromConfig(String section, Player p) {
	    Map<Integer, ItemStack> itemMap = new HashMap<>();
	    Integration integration = Main.get().getIntegration();
	    final MPlayer p2 = MPlayer.get((Object) p);

	    // Obter a facção do cache CORRETAMENTE
	    FactionModel factionModel = null;
	    HashMap<String, FactionModel> cache = (HashMap) FactionCache.get(FactionCache::getCache);
	    if (cache != null) {
	        factionModel = cache.get(integration.getTag(p));
	    } 

	    for (String itemSection : m.getSection(section).getKeys(false)) {
	        String path = section + "." + itemSection + ".";
	        int slot = m.getInt(path + "slot");
	        String materialData = m.getString(path + "Material");
	        ItemStack item = Optional.of(materialData)
	                .filter(data -> data.contains(":"))
	                .map(data -> new MaterialData(Integer.parseInt(data.split(":")[0]), Byte.parseByte(data.split(":")[1])).toItemStack(1))
	                .orElseGet(() -> materialData.equals("{banner}") ? Banner.getWhiteBanner(p2.getFactionTag()) : new ItemStack(SkullAPI.getSkull(materialData)));
	        ItemMeta meta = item.getItemMeta();
	        meta.setDisplayName(m.getString(path + "name").replace("&", "§").replace("{fac}", p2.getFactionTag()));
	        List<String> lore = new ArrayList<>();
	        for (String loreLine : m.getStringList(path + "lore")) {
	            lore.add(loreLine.replace("&", "§")
	                    .replace("{fac}", p2.getFactionTag())
	                    .replace("{moedas}", FormatterAPI.formatter(factionModel != null ? factionModel.getMoedas() : 0)));
	        }
	        meta.setLore(lore);
	        item.setItemMeta(meta);
	        itemMap.put(slot, item);
	    }
	    return itemMap;
	}
	
}
