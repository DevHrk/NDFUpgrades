package me.nd.upgrades.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.entity.MPlayer;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.ActionBarAPI;
import me.nd.upgrades.api.FormatterAPI;
import me.nd.upgrades.api.TitleAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.invetory.MainInventory;
import me.nd.upgrades.invetory.Ranking;
import me.nd.upgrades.invetory.UpgradesInventory;
import me.nd.upgrades.invetory.UtilsInventory;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.plugin.SConfig;

public class CommandsUtils {
    
	private static final SConfig m = Main.get().getConfig("config");
	private static final SConfig config1 = Main.get().getConfig("Mensagens");
	private static final SConfig config2 = Main.get().getConfig("Menus", "MenuFuncoes");
    public static void send(Player p, Object... rawmsg) {
        Stream.of(rawmsg)
            .flatMap(o -> o instanceof List ? ((List<?>) o).stream() : Stream.of(o))
            .filter(o -> o instanceof String)
            .map(o -> (String) o)
            .forEach(msg -> processMessage(p, msg));
    }

    private static void processMessage(Player p, String msg) {
        String message = msg.replace("{player}", p.getName()).replace("\\n", "\n").replace("&", "§");

        MessageHandler handler = MessageHandlers.getHandler(message);
        if (handler != null) {
            handler.handle(p, message);
        }
    }

    @FunctionalInterface
    interface MessageHandler {
        void handle(Player p, String message);
    }

    enum MessageHandlers implements MessageHandler {
        CONSOLE("console: ", (p, message) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message)),
        CHAT("player: ", (p, message) -> p.chat("/" + message)),
        CLOSEMENU("CLOSEMENU", (p, message) -> p.closeInventory()),
        PLANTACAO("PLANTACAO", (p, message) -> new UpgradesInventory().plantacao(p)),
        VOAR("VOAR", (p, message) -> new UpgradesInventory().voar(p)),
        MOBS("MOBS", (p, message) -> new UpgradesInventory().mobs(p)),
        PODER("PODER", (p, message) -> new UpgradesInventory().poder(p)),
        MEMBROS("MEMBROS", (p, message) -> new UpgradesInventory().membro(p)),
        BLOCOS("BLOCOS", (p, message) -> new UpgradesInventory().blocos(p)),
        ATTACK("ATTACK", (p, message) -> new UpgradesInventory().attack(p)),
        DEFESAS("DEFESAS", (p, message) -> new UpgradesInventory().defesas(p)),
        PRINCIPAL("PRINCIPAL", (p, message) -> MainInventory.openPrincipal(p)),
        UPGRADES("UPGRADES", (p, message) -> MainInventory.openUpgrades(p)),
        RANKINGMOEDAS("RANKINGMOEDAS", (p, message) -> Ranking.open(p)),
        FUNCOES("FUNCOES", (p, message) -> UtilsInventory.open(p)),
        MENSAGEM("mensagem: ", (p, message) -> p.sendMessage(message.replace("mensagem: ", ""))),
        BROADCAST("broadcast: ", (p, message) -> Bukkit.broadcastMessage(message.replace("broadcast: ", ""))),
        JSON("json: ", (p, message) -> JsonMessage.send(p, message.replace("json: ", ""))),
        ACTION_BAR("actionbar: ", (p, message) -> ActionBarAPI.sendActionBarMessage(p, message.replace("actionbar: ", ""))),
        TITLE("title: ", (p, message) -> TitleAPI.sendTitle(p, 20, 30, 20, message.replace("title: ", ""), "")),
        RETIRARFRAG("RETIRARFRAG", (p, message) -> {
            Integration integration = Main.get().getIntegration();
            final FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            if (p.getInventory().firstEmpty() == -1) {
                MessageUtils.send(p, config1.getString("mensagem.invlotado"), config1.getStringList("mensagem.invlotado"));
                return;
            }
            int amount = p.isSneaking() ? factionModel.getMoedas() : config2.getInt("Menu-Funcoes.itens.Retirar.retirar");
            if (factionModel.getMoedas() < amount) {
                MessageUtils.send(p, config1.getString("Moeda.semmoedas"), config1.getStringList("Moeda.semmoedas"));
                return;
            }
            List<String> lore = m.getStringList("Moeda.Lore");
            List<String> replacedLore = lore.stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            factionModel.setMoedas(factionModel.getMoedas() - amount);
            p.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DOUBLE_PLANT).setDurability((short)11).setAmount(amount).setName(m.getString("Moeda.Nome").replace("&", "§")).setLore(replacedLore).toItemStack() });
            MessageUtils.send(p, config1.getString("Moeda.retiroumoedas").replace("{amount}", String.valueOf(FormatterAPI.formatter(amount))), config1.getStringList("Moeda.retiroumoedas").stream().map(s -> s.replace("{amount}", String.valueOf(FormatterAPI.formatter(amount)))).collect(Collectors.toList()));
        }),
        RESTRINGIR("RESTRINGIR", (p, message) -> {
        	Integration integration = Main.get().getIntegration();
        	final MPlayer p2 = MPlayer.get((Object)p);
            final FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            if (p2.getFaction().getAllys().size() == 0) {
            	MessageUtils.send(p, config1.getString("RestringirEfeitos.SemAlido"),config1.getStringList("RestringirEfeitos.SemAlido"));
                return;
            }
            if (factionModel.getAlly() == 0) {
            	factionModel.setAlly(1);
            }else if (factionModel.getAlly() == 1) {
            	      factionModel.setAlly(0);
            }
            MessageUtils.send(p, config1.getString("RestringirEfeitos.Sucesso"),config1.getStringList("RestringirEfeitos.Sucesso"));
            p.closeInventory();
            UtilsInventory.open(p);
        }),
        TROCARFRAG("TROCARFRAG", (p, message) -> {
            final ItemStack item = new ItemStack(Material.INK_SACK, config2.getInt("Menu-Funcoes.itens.Trocar.trocar"));
            final ItemMeta meta = item.getItemMeta();
            item.setDurability((short)11);
            meta.setDisplayName(m.getString("Fragmento.Nome").replace("&", "§"));
            ArrayList<String> l2 = new ArrayList<String>();
            for (String lore122 : m.getStringList("Fragmento.Lore")) {
     		   l2.add(lore122.replace("&", "§"));
            meta.setLore(l2);
            item.setItemMeta(meta);
            }
            if (p.getInventory().firstEmpty() == -1) {
            	MessageUtils.send(p, config1.getString("mensagem.invlotado"),config1.getStringList("mensagem.invlotado"));
                return;
            }
            if (!FactionUtils.getUtils().hasItem(p, item, config2.getInt("Menu-Funcoes.itens.Trocar.trocar"))) {
            	MessageUtils.send(p, config1.getString("Fragmentos.semfragmentos"),config1.getStringList("Fragmentos.semfragmentos"));
                return;
            }                        
            List<String> lore = m.getStringList("Moeda.Lore");
            List<String> replacedLore = lore.stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            
            p.getInventory().removeItem(new ItemStack[] { item });
            p.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.DOUBLE_PLANT).setDurability((short)11).setAmount(config2.getInt("Menu-Funcoes.itens.Trocar.retirar")).setName(m.getString("Moeda.Nome").replace("&", "§")).setLore(replacedLore).toItemStack()
            		});
        }),
        TITLE_SUB("titlesub: ", (p, message) -> {
            String[] parts = message.replace("titlesub: ", "").split(":");
            TitleAPI.sendTitle(p, 20, 30, 20, parts[0], parts.length > 1 ? parts[1] : "");
        });
    	
        private final String prefix;
        private final BiConsumer<Player, String> handler;

        MessageHandlers(String prefix, BiConsumer<Player, String> handler) {
            this.prefix = prefix;
            this.handler = handler;
        }

        public static MessageHandler getHandler(String message) {
            for (MessageHandlers handler : values()) {
                if (message.startsWith(handler.prefix)) {
                    return handler;
                }
            }
            return null;
        }

        @Override
        public void handle(Player p, String message) {
            handler.accept(p, message.replace(prefix, ""));
        }
    }

}
