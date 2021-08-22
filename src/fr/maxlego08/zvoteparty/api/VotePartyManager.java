package fr.maxlego08.zvoteparty.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface VotePartyManager{

	/**
	 * Allows to reload configuration files
	 * 
	 * @param sender - Person who will execute the command
	 */
	void reload(CommandSender sender);
	
	/*
	 * Allows to load the configuration of the plugin
	 */
	void loadConfiguration();

	/**
	 * Allows you to open the voting inventory
	 * 
	 * @param player
	 */
	void vote(Player player);

}
