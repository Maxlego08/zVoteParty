package fr.maxlego08.zvoteparty.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class AdapterListener extends ZUtils implements Listener {

	private final ZVotePartyPlugin template;

	public AdapterListener(ZVotePartyPlugin template) {
		this.template = template;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, event.getPlayer()));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onQuit(event, event.getPlayer()));
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryClick(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryDrag(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event, (Player) event.getPlayer()));
	}
	
	@EventHandler
	public void onVote(VotifierEvent event){
		Vote vote = event.getVote();
		template.getListenerAdapters().forEach(adapter -> adapter.onVote(event, vote, vote.getUsername(), vote.getServiceName()));
	}
}
