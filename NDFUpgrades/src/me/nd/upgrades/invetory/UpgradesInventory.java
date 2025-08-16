package me.nd.upgrades.invetory;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.FormatterAPI;
import me.nd.upgrades.cache.FactionCache;
import me.nd.upgrades.cache.UpgradesCache;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.model.UpgradesModel;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;
import me.nd.upgrades.utils.Scroller;

public class UpgradesInventory {
	
    public void voar(final Player p) {
    	SConfig m = Main.get().getConfig("Upgrades");
    	SConfig m1 = Main.get().getConfig("Mensagens");
        final Integration integration = Main.get().getIntegration();
        FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
        if (factionModel.getVoar() == 0) {
            if (factionModel.getMoedas() >= m.getInt("Upgrades.Voar.upgrade1.price")) {
                factionModel.setVoar(m.getInt("Upgrades.Voar.upgrade1.level"));
                factionModel.setMoedas(factionModel.getMoedas() - m.getInt("Upgrades.Voar.upgrade1.price"));
                MessageUtils.send(p, m.getString("Upgrades.Voar.upgrade1.title"), m.getStringList("Upgrades.Voar.upgrade1.title"));
                p.closeInventory();
            } else {
            	MessageUtils.send(p, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            }
        } else {
        	MessageUtils.send(p, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
        }
    }

    
    public void plantacao(final Player p) {
    	SConfig m = Main.get().getConfig("Upgrades");
    	SConfig m1 = Main.get().getConfig("Mensagens");
        final Integration integration = Main.get().getIntegration();
        final FactionModel factionModel = FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
        if (factionModel.getPlantacao() == 0) {
            if (factionModel.getMoedas() >= m.getInt("Upgrades.Plantacoes.upgrade1.price")) {
                factionModel.setPlantacao(m.getInt("Upgrades.Plantacoes.upgrade1.level"));
                factionModel.setMoedas(factionModel.getMoedas() - m.getInt("Upgrades.Plantacoes.upgrade1.price"));
                MessageUtils.send(p, m.getString("Upgrades.Plantacoes.upgrade1.title"),m.getStringList("Upgrades.Plantacoes.upgrade1.title"));
                p.closeInventory();
            } else {
            	MessageUtils.send(p, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            }
        } else {
        	MessageUtils.send(p, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
        }
    }
    
    public void poder(Player p) {
    	SConfig m1 = Main.get().getConfig("Mensagens");
    	SConfig m2 = Main.get().getConfig("Upgrades");
    	FileConfiguration m = Main.get().getConfig();
    	MPlayer mp = MPlayer.get(p);
        ConfigurationSection configurationSection = m2.getSection("Upgrades");
        Integration integration = Main.get().getIntegration();
        FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
        ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
        configurationSection.getConfigurationSection("poder").getKeys(false).forEach(key -> {
            UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getPoder).get(key);
            
            if (factionModel.getPoder() >= upgradesModel.getLevel()) {
                ItemStack item = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Poder", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá receber +1 de poder no valor total.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§aVocê ja comprou este upgrade.").toItemStack();
                item.setDurability((short)5);
                itens.add(item);
            } else {
                ItemStack item1 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Poder", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá receber +1 de poder no valor total.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§eVocê não possui este upgrade.").toItemStack();
                item1.setDurability((short)14);
                itens.add(item1);
            }
        });
        Scroller scroller = new Scroller.ScrollerBuilder().withSize(45).withName("[" + integration.getTag(p) + "] Upgrades > Poder").withItems(itens).withItemsSlots(11, 12, 13, 14, 15).withArrowsSlots(9, 17).withOnClick((player, item, click) -> {
            for (String key : m2.getSection("Upgrades").getConfigurationSection("poder").getKeys(false)) {
                UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getPoder).get(key);
                if (factionModel.getPoder() < m.getInt("Config.maxlevelupgrades.poder")) {
                    if (factionModel.getPoder() + 1 != upgradesModel.getLevel()) continue;
                    if ((double)factionModel.getMoedas().intValue() >= upgradesModel.getPrice()) {
                        factionModel.setMoedas((int)((double)factionModel.getMoedas().intValue() - upgradesModel.getPrice()));
                        factionModel.setPoder(factionModel.getPoder() + 1);
                        mp.setPowerBoost(mp.getPowerBoost() + 1.0);
                        MessageUtils.send(player, m1.getString("mensagem.upgradecomprado"),m1.getStringList("mensagem.upgradecomprado"));
                        player.closeInventory();
                        poder(player);
                        return;
                    }
                }else {
                	MessageUtils.send(player, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
                }
            }
            MessageUtils.send(player, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
        }).build(p, ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getPoder());
        scroller.open(p);
    }
    public void membro(Player p) {
    	FileConfiguration m = Main.get().getConfig();
    	SConfig m1 = Main.get().getConfig("Mensagens");
    	SConfig m2 = Main.get().getConfig("Upgrades");
    	MPlayer mp = MPlayer.get(p);
    	Faction f = mp.getFaction();
            ConfigurationSection configurationSection = m2.getSection("Upgrades");
            Integration integration = Main.get().getIntegration();
            FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            ArrayList<ItemStack> itens1 = new ArrayList<ItemStack>();
            configurationSection.getConfigurationSection("membros").getKeys(false).forEach(key -> {
                UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getMembros).get(key);
                if (factionModel.getMembros() >= upgradesModel.getLevel()) {
                    ItemStack item2 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Membros", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá receber +1 vaga de membro no valor total.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§aVocê ja comprou este upgrade.").toItemStack();
                    item2.setDurability((short)5);
                    itens1.add(item2);
                } else {
                    ItemStack item3 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Membros", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá receber +1 vaga de membro no valor total.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§eVocê não possui este upgrade.").toItemStack();
                    item3.setDurability((short)14);
                    itens1.add(item3);
                }
            });
            Scroller scroller1 = new Scroller.ScrollerBuilder().withSize(45).withName("[" + integration.getTag(p) + "] Upgrades > Membros").withItems(itens1).withItemsSlots(11, 12, 13, 14, 15).withArrowsSlots(9, 17).withOnClick((player1, item, click) -> {
                for (String key : m2.getSection("Upgrades").getConfigurationSection("membros").getKeys(false)) {
                    UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getMembros).get(key);
                    if (factionModel.getMembros() < m.getInt("Config.maxlevelupgrades.membros")) {
                        if (factionModel.getMembros() + 1 != upgradesModel.getLevel()) continue;
                        if ((double)factionModel.getMoedas().intValue() >= upgradesModel.getPrice()) {
                            factionModel.setMoedas((int)((double)factionModel.getMoedas().intValue() - upgradesModel.getPrice()));
                            factionModel.setMembros(factionModel.getMembros() + 1);
                        f.setMemberBoost(f.getMemberBoost() + 1);
                        MessageUtils.send(player1, m1.getString("mensagem.upgradecomprado"),m1.getStringList("mensagem.upgradecomprado"));
                        player1.closeInventory();
                        membro(player1);
                        return;
                    }
                    }else {
                    	MessageUtils.send(player1, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
                    }
                }
                MessageUtils.send(player1, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            }).build(p, ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getMembros());
        scroller1.open(p);
    }

    public void mobs(Player p) {
    	FileConfiguration m = Main.get().getConfig();
    	SConfig m1 = Main.get().getConfig("Mensagens");
    	SConfig m2 = Main.get().getConfig("Upgrades");
            ConfigurationSection configurationSection = m2.getSection("Upgrades");
            Integration integration = Main.get().getIntegration();
            FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
            configurationSection.getConfigurationSection("mobs").getKeys(false).forEach(key -> {
                UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getMobs).get(key);
                if (factionModel.getMobs() >= upgradesModel.getLevel()) {
                    ItemStack item = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Mobs", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f poderá colocar os spawners do tipo §f" + upgradesModel.getType(), "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§aVocê ja comprou este upgrade.").toItemStack();
                    item.setDurability((short)5);
                    itens.add(item);
                } else {
                    ItemStack item = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Mobs", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f poderá colocar os spawners do tipo §f" + upgradesModel.getType(), "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§eVocê não possui este upgrade.").toItemStack();
                    item.setDurability((short)14);
                    itens.add(item);
                }
            });
            Scroller scroller = new Scroller.ScrollerBuilder().withSize(45).withName("[" + integration.getTag(p) + "] Upgrades > Mobs").withItems(itens).withItemsSlots(11, 12, 13, 14, 15).withArrowsSlots(9, 17).withOnClick((player, item, click) -> {
                for (String key : m2.getSection("Upgrades").getConfigurationSection("mobs").getKeys(false)) {
                    UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getMobs).get(key);
                    if (factionModel.getMobs() < m.getInt("Config.maxlevelupgrades.mobs")) {
                        if (factionModel.getMobs() + 1 != upgradesModel.getLevel()) continue;
                        if ((double)factionModel.getMoedas().intValue() >= upgradesModel.getPrice()) {
                            factionModel.setMoedas((int)((double)factionModel.getMoedas().intValue() - upgradesModel.getPrice()));
                            factionModel.setMobs(factionModel.getMobs() + 1);
                            MessageUtils.send(player, m1.getString("mensagem.upgradecomprado"),m1.getStringList("mensagem.upgradecomprado"));
                            player.closeInventory();
                            this.mobs(player);
                            return;
                        }
                    }else {
                    	MessageUtils.send(player, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
                    }
                }
                MessageUtils.send(player, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            }).build(p, ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getMobs());
        scroller.open(p);
    }    

    public void blocos(Player p) {
    	FileConfiguration m = Main.get().getConfig();
    	SConfig m1 = Main.get().getConfig("Mensagens");
    	SConfig m2 = Main.get().getConfig("Upgrades");
            ConfigurationSection configurationSection = m2.getSection("Upgrades");
            Integration integration = Main.get().getIntegration();
            FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            ArrayList<ItemStack> itens1 = new ArrayList<ItemStack>();
            configurationSection.getConfigurationSection("blocos").getKeys(false).forEach(key -> {
                UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getBlocos).get(key);
                if (factionModel.getBlocos() >= upgradesModel.getLevel()) {
                    ItemStack item2 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Blocos", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá receber a permissão de colocar uma §f" + upgradesModel.getType(), "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§aVocê ja comprou este upgrade.").toItemStack();
                    item2.setDurability((short)5);
                    itens1.add(item2);
                } else {
                    ItemStack item3 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Blocos", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá receber a permissão de colocar uma §f" + upgradesModel.getType(), "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§eVocê não possui este upgrade.").toItemStack();
                    item3.setDurability((short)14);
                    itens1.add(item3);
                }
            });
            Scroller scroller1 = new Scroller.ScrollerBuilder().withSize(45).withName("[" + integration.getTag(p) + "] Upgrades > Blocos").withItems(itens1).withItemsSlots(11, 12, 13, 14, 15).withArrowsSlots(9, 17).withOnClick((player1, item, click) -> {
                for (String key : m2.getSection("Upgrades").getConfigurationSection("blocos").getKeys(false)) {
                    UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getBlocos).get(key);
                    if (factionModel.getBlocos() < m.getInt("Config.maxlevelupgrades.blocos")) {
                        if (factionModel.getBlocos() + 1 != upgradesModel.getLevel()) continue;
                        if ((double)factionModel.getMoedas().intValue() >= upgradesModel.getPrice()) {
                            factionModel.setMoedas((int)((double)factionModel.getMoedas().intValue() - upgradesModel.getPrice()));
                            factionModel.setBlocos(factionModel.getBlocos() + 1);
                        MessageUtils.send(player1, m1.getString("mensagem.upgradecomprado"),m1.getStringList("mensagem.upgradecomprado"));
                        player1.closeInventory();
                        blocos(player1);
                        return;
                    }
                    }else {
                    	MessageUtils.send(player1, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
                    }
                }
                MessageUtils.send(player1, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            
            }).build(p, ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getBlocos());
        scroller1.open(p);
    }
    
    public void attack(Player p) {
    	FileConfiguration m = Main.get().getConfig();
    	SConfig m1 = Main.get().getConfig("Mensagens");
    	SConfig m2 = Main.get().getConfig("Upgrades");
    	ConfigurationSection configurationSection = m2.getSection("Upgrades");
            Integration integration = Main.get().getIntegration();
            FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            ArrayList<ItemStack> itens1 = new ArrayList<ItemStack>();
            configurationSection.getConfigurationSection("attack").getKeys(false).forEach(key -> {
                UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getAttack).get(key);
                if (factionModel.getAttack() >= upgradesModel.getLevel()) {
                    ItemStack item2 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Attack", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá diminuir o tempo de attack de sua base.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§aVocê ja comprou este upgrade.").toItemStack();
                    item2.setDurability((short)5);
                    itens1.add(item2);
                } else {
                    ItemStack item3 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Attack", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá diminuir o tempo de attack de sua base.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()), "", "§eVocê não possui este upgrade.").toItemStack();
                    item3.setDurability((short)14);
                    itens1.add(item3);
                }
            });
            
            Scroller scroller1 = new Scroller.ScrollerBuilder().withSize(45).withName("[" + integration.getTag(p) + "] Upgrades > Attack").withItems(itens1).withItemsSlots(11, 12, 13, 14, 15).withArrowsSlots(9, 17).withOnClick((player1, item, click) -> {
                for (String key : m2.getSection("Upgrades").getConfigurationSection("attack").getKeys(false)) {
                    UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getAttack).get(key);
                    if (factionModel.getAttack() < m.getInt("Config.maxlevelupgrades.attack")) {
                        if (factionModel.getAttack() + 1 != upgradesModel.getLevel()) continue;
                        if ((double)factionModel.getMoedas().intValue() >= upgradesModel.getPrice()) {
                            factionModel.setMoedas((int)((double)factionModel.getMoedas().intValue() - upgradesModel.getPrice()));
                            factionModel.setAttack(factionModel.getAttack() + 1);
                        MessageUtils.send(player1, m1.getString("mensagem.upgradecomprado"),m1.getStringList("mensagem.upgradecomprado"));
                        player1.closeInventory();
                        attack(player1);
                        return;
                    }
                    }else {
                    	MessageUtils.send(player1, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
                    }
                }
                MessageUtils.send(player1, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            }).build(p, ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getAttack());
        scroller1.open(p);
    }
    
    public void defesas(Player p) {
    	FileConfiguration m = Main.get().getConfig();
    	SConfig m1 = Main.get().getConfig("Mensagens");
    	SConfig m2 = Main.get().getConfig("Upgrades");
    	ConfigurationSection configurationSection = m2.getSection("Upgrades");
            Integration integration = Main.get().getIntegration();
            FactionModel factionModel = (FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p));
            ArrayList<ItemStack> itens1 = new ArrayList<ItemStack>();
            configurationSection.getConfigurationSection("defesas").getKeys(false).forEach(key -> {
                UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getDefesas).get(key);
                if (factionModel.getDefesas() >= upgradesModel.getLevel()) {
                    ItemStack item2 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Defesas", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá adicionar uma defesa a mais a sua base.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()),"§f Efeito: §e" + upgradesModel.getType(), "", "§aVocê ja comprou este upgrade.").toItemStack();
                    item2.setDurability((short)5);
                    itens1.add(item2);
                } else {
                    ItemStack item3 = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(upgradesModel.getTitle()).setLore("§8Upgrade de Defesas", "", "§aDescrição:", "§f Comprando este upgrade sua facção", "§f irá adicionar uma defesa a mais a sua base.", "", "§aInformações", "§f Valor do upgrade: §6" + FormatterAPI.formatter(upgradesModel.getPrice()),"§f Efeito: §e" + upgradesModel.getType(), "", "§eVocê não possui este upgrade.").toItemStack();
                    item3.setDurability((short)14);
                    itens1.add(item3);
                }
            });
            
            Scroller scroller1 = new Scroller.ScrollerBuilder().withSize(45).withName("[" + integration.getTag(p) + "] Upgrades > Defesas").withItems(itens1).withItemsSlots(11, 12, 13, 14, 15).withArrowsSlots(9, 17).withOnClick((player1, item, click) -> {
                for (String key : m2.getSection("Upgrades").getConfigurationSection("defesas").getKeys(false)) {
                    UpgradesModel upgradesModel = (UpgradesModel)UpgradesCache.get(UpgradesCache::getDefesas).get(key);
                    if (factionModel.getDefesas() < m.getInt("Config.maxlevelupgrades.defesas")) {
                        if (factionModel.getDefesas() + 1 != upgradesModel.getLevel()) continue;
                        if ((double)factionModel.getMoedas().intValue() >= upgradesModel.getPrice()) {
                            factionModel.setMoedas((int)((double)factionModel.getMoedas().intValue() - upgradesModel.getPrice()));
                            factionModel.setDefesas(factionModel.getDefesas() + 1);
                        MessageUtils.send(player1, m1.getString("mensagem.upgradecomprado"),m1.getStringList("mensagem.upgradecomprado"));
                        player1.closeInventory();
                        defesas(player1);
                        return;
                    }
                    }else {
                    	MessageUtils.send(player1, m1.getString("mensagem.levelmax"),m1.getStringList("mensagem.levelmax"));
                    }
                }
                MessageUtils.send(player1, m1.getString("Moeda.semmoedas"),m1.getStringList("Moeda.semmoedas"));
            }).build(p, ((FactionModel)FactionCache.get(FactionCache::getCache).get(integration.getTag(p))).getDefesas());
        scroller1.open(p);
    }
    
}