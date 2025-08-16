package me.nd.upgrades.list;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;

import me.nd.upgrades.Main;
import me.nd.upgrades.dao.FactionDAO;
import me.nd.upgrades.integration.Integration;
import me.nd.upgrades.model.FactionModel;
import me.nd.upgrades.utils.FactionUtils;

public class FacEvent implements Listener {
	
	@EventHandler
	void FactionCreate(EventFactionsCreate e) {
	    String factionTag = e.getFactionTag();
	    FactionUtils.getUtils().addFactionToCache(factionTag);
	}

	@EventHandler
	void FactionDisband(EventFactionsDisband e) {
	    String factionTag = e.getFaction().getTag();
	    FactionUtils.getUtils().removeFactionToCache(factionTag);
	    FactionModel factionModel = FactionModel.builder().tag(factionTag).build();
	    FactionDAO.deleteFac(factionModel);
	}

	@EventHandler
	void PlayerLogin(final PlayerLoginEvent e) {
		final Integration integration = Main.get().getIntegration();
        // Verificar se a facção existe no banco de dados
        if (!FactionUtils.getUtils().factionExistsInDB(integration.getTag(e.getPlayer()))) {
            FactionUtils.getUtils().addFactionToCach(integration.getTag(e.getPlayer()));
        }
        if (Main.get().getIntegration().hasFaction(e.getPlayer())) {
            FactionUtils.getUtils().addFactionToCache(integration.getTag(e.getPlayer()));
        }
	}
	@EventHandler
	void PlayerJoin(final PlayerJoinEvent e) {
	    final Integration integration = Main.get().getIntegration();
	    final String factionTag = integration.getTag(e.getPlayer());

	    if (!FactionUtils.getUtils().factionExistsInDB(factionTag)) {
	        FactionUtils.getUtils().addFactionToCache(factionTag);
	    }
	}
}