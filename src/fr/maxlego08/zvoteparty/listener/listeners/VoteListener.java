package fr.maxlego08.zvoteparty.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.listener.ListenerAdapter;

public class VoteListener extends ListenerAdapter {

	private final ZVotePartyPlugin plugin;

	/**
	 * @param plugin
	 */
	public VoteListener(ZVotePartyPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		VotePartyManager manager = this.plugin.getManager();
		manager.giveVotes(player);
	}
	
}
