package fr.maxlego08.zvoteparty;

import org.bukkit.command.CommandSender;

import fr.maxlego08.zvoteparty.api.VotePartyManager;

public class ZVotePartyManager implements VotePartyManager {

	private final ZVotePartyPlugin plugin;

	/**
	 * @param plugin
	 */
	public ZVotePartyManager(ZVotePartyPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void reload(CommandSender sender) {

	}

}
