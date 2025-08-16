package me.nd.upgrades.invetory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.Banner;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.FormatterAPI;
import me.nd.upgrades.api.SkullAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.CommandsUtils;
import me.nd.upgrades.utils.ScrollerRanking;

public class Ranking {
	
	private static final SConfig m = Main.get().getConfig("Menus", "MenuRanking");
	private static final List<Integer> SLOTS = Arrays.stream(m.getString("Menu-Ranking.top.slots").split(", ")).map(Integer::parseInt).collect(Collectors.toList());
		@SuppressWarnings("unused")
		public static void open(Player p) {
		    AtomicInteger pos = new AtomicInteger(1);
		    final MPlayer p2 = MPlayer.get((Object)p);
		    final List<ItemStack> itens = new ArrayList<ItemStack>();
		    Integration integration = Main.get().getIntegration();

		    // Get all factions
		    Map<String, FactionModel> factions = (Map<String, FactionModel>) FactionCache.get(FactionCache::getCache);

		    // Iterate over all factions and create items
		    for (Map.Entry<String, FactionModel> entry : factions.entrySet()) {
		        String factionTag = entry.getKey();
		        FactionModel faction = entry.getValue();
		        ItemStack item = new ItemStack(Banner.getWhiteBanner(factionTag));
		        ItemMeta meta = item.getItemMeta();
		        meta.setDisplayName(m.getString("Menu-Ranking.top.name").replace("&", "§").replace("{pos}", pos + "").replace("{tag}", factionTag).replace("{fac}", faction.getTag()));
		        ArrayList<String> lore = new ArrayList<String>();
		        for (String lorez3 : m.getStringList("Menu-Ranking.top.lore")) {
		            lore.add(lorez3.replace("&", "§")
		                   .replace("{moedas}", FormatterAPI.formatter(faction.getMoedas().intValue())));
		        }
		        meta.setLore(lore);
		        item.setItemMeta(meta);
		        itens.add(item);
		        pos.getAndIncrement();
		    }
        ScrollerRanking scroller = new ScrollerRanking.ScrollerBuilder().withSize(m.getInt("Menu-Ranking.tamanho")).withName(m.getString("Menu-Ranking.nome_menu").replace("&", "§")).withItems(itens).withItemsSlots(SLOTS).withArrowsSlots(m.getInt("Menu-Ranking.top.BackSlot"), m.getInt("Menu-Ranking.top.NextSlot")).build();
        scroller.open(p);                                                              
                                                                                                
	    // Adiciona os itens configurados ao menu
	    Map<Integer, ItemStack> additionalItems = createItemsFromConfig("Menu-Ranking.itens");
	    additionalItems.forEach((slot, ite) -> {
	        ScrollerRanking.addItemToMenu(scroller, ite, slot);
	        String itemSection = null;
	        for (String section : m.getSection("Menu-Ranking.itens").getKeys(false)) {
	            if (m.getString("Menu-Ranking.itens." + section + ".slot").equals(String.valueOf(slot))) {
	                itemSection = section;
	                break;
	            }
	        }
	        if (itemSection!= null && m.contains("Menu-Ranking.itens." + itemSection + ".onClick")) {
	            List<String> onClickCommand = m.getStringList("Menu-Ranking.itens." + itemSection + ".onClick");
	            ScrollerRanking.addClickFunctionality(scroller, ite, slot, player -> {
	            CommandsUtils.send(player, onClickCommand);
	            });
	        }
	    });
	}
	
	public static Map<Integer, ItemStack> createItemsFromConfig(String section) {
	    Map<Integer, ItemStack> itemMap = new HashMap<>();
	    for (String itemSection : m.getSection(section).getKeys(false)) {
	        String path = section + "." + itemSection + ".";
	        int slot = m.getInt(path + "slot");
	        String materialData = m.getString(path + "Material");
	        ItemStack item = Optional.of(materialData).filter(data -> data.contains(":")).map(data -> new MaterialData(Integer.parseInt(data.split(":")[0]), Byte.parseByte(data.split(":")[1])).toItemStack(1)).orElseGet(() -> new ItemStack(SkullAPI.getSkull(materialData)));
	        ItemMeta meta = item.getItemMeta();
	        meta.setDisplayName(m.getString(path + "name").replace("&", "§"));
	        List<String> lore = new ArrayList<>();
	        for (String loreLine : m.getStringList(path + "lore")) {
	            lore.add(loreLine.replace("&", "§"));
	        }
	        meta.setLore(lore);
	        item.setItemMeta(meta);
	        itemMap.put(slot, item);
	    }
	    return itemMap;
	}
}
