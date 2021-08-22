package fr.maxlego08.zvoteparty;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.listener.ListenerAdapter;
import fr.maxlego08.zvoteparty.save.Config;

public class ZVotePartyManager extends ListenerAdapter implements VotePartyManager {

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

		try {

			this.plugin.reloadConfig();
			this.plugin.getInventoryManager().loadInventories();
			this.loadConfiguration();
			message(sender, Message.RELOAD_SUCCESS);

		} catch (Exception e) {

			e.printStackTrace();
			message(sender, Message.RELOAD_SUCCESS);

		}

	}

	@Override
	public void loadConfiguration() {
		// TODO Auto-generated method stub

	}

	@Override
	public void vote(Player player) {
		if (Config.enableVoteMessage)
			message(player, Message.VOTE_INFORMATIONS);
	}

}
