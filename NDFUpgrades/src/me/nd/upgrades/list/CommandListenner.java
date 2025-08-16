package me.nd.upgrades.list;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MPlayer;

import me.nd.upgrades.Main;
import me.nd.upgrades.invetory.MainInventory;
import me.nd.upgrades.plugin.SConfig;
import me.nd.upgrades.utils.MessageUtils;

public class CommandListenner implements Listener
{
    @EventHandler
    void a(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        final MPlayer p2 = MPlayer.get((Object)p);
        SConfig m1 = Main.get().getConfig("Mensagens");
        if (e.getMessage().startsWith("/f upgrade") || e.getMessage().startsWith("/f upgrades")) {
            e.setCancelled(true);
            if (!p2.hasFaction()) {
            	MessageUtils.send(p, m1.getString("mensagem.possuirfac"),m1.getStringList("mensagem.possuirfac"));
                return;
            }
            if (p2.getRole() == Rel.MEMBER || p2.getRole() == Rel.RECRUIT) {
            	MessageUtils.send(p, m1.getString("mensagem.superior"),m1.getStringList("mensagem.superior"));
                return;
            }
            new MainInventory();
            MainInventory.openPrincipal(p);
            MessageUtils.send(p, m1.getString("mensagem.menuaberto"),m1.getStringList("mensagem.menuaberto"));
        }
    }
}
