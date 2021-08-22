package fr.maxlego08.zvoteparty.api;

import org.bukkit.command.CommandSender;

public interface VotePartyManager{

	/**
	 * Allows to reload configuration files
	 * 
	 * @param sender - Person who will execute the command
	 */
	void reload(CommandSender sender);

}
