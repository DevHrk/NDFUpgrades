package me.nd.upgrades.command;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.nd.upgrades.Main;
import me.nd.upgrades.api.FormatterAPI;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageSenderUtils;

public class Removedor extends Commands {
	public Removedor() {
        super("giveremovedor");
    }

	public void perform(CommandSender s, String lb, String[] args) {
	    FileConfiguration m = Main.get().getConfig();
	    SConfig m1 = Main.get().getConfig("Mensagens");
	    SConfig ps = Main.get().getConfig("Utils","Permissoes");
	    if (!s.hasPermission(ps.getString("giveremovedor"))) {
	    	MessageSenderUtils.send(s, m1.getString("mensagem.semperm"),m1.getStringList("mensagem.semperm"));
	        return;
	    }
	    if (args.length < 2) {
	    	MessageSenderUtils.send(s, m1.getString("Comando-Errado.removedor") ,m1.getStringList("Comando-Errado.removedor"));
	        return;
	    } 
	    if (args.length == 2) {
	        String targetPlayerName = args[0];
	        int quantia = Integer.parseInt(args[1]);
	        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
	        if (targetPlayer == null) {
	            MessageSenderUtils.send(s, m1.getString("mensagem.jogador") ,m1.getStringList("mensagem.jogador"));
	            return;
	        } 
	        ItemStack item3 = new ItemStack(Material.getMaterial(m.getInt("RemovedorEfeito.Id")), quantia);
	        ItemMeta i3 = item3.getItemMeta();
	        item3.setDurability((short) m.getInt("RemovedorEfeito.Data"));
	        i3.setDisplayName(m.getString("RemovedorEfeito.Nome").replace("&", "§"));
	        ArrayList<String> l3 = new ArrayList<String>();
	        for (String lore122 : m.getStringList("RemovedorEfeito.Lore")) {
	            l3.add(lore122.replace("&", "§"));
	        }
	        i3.setLore(l3);
	        item3.setItemMeta(i3);
	        targetPlayer.getInventory().addItem(new ItemStack[]{item3});
	        MessageSenderUtils.send(s, m1.getString("removedor.enviado").replace("{player}", targetPlayer.getName()).replace("{quantia}", FormatterAPI.formatter(quantia)) ,m1.getStringList("removedor.enviado").stream().map(message -> message.replace("{player}", targetPlayer.getName()).replace("{quantia}", FormatterAPI.formatter(quantia))).collect(Collectors.toList()));
	    }
	}
}
