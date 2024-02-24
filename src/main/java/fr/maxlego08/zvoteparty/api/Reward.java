package fr.maxlego08.zvoteparty.api;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public interface Reward {

	/**
	 * Returns the percentage chance of getting the reward
	 * 
	 * @return percent
	 */
	double getPercent();
	
	/**
	 * Returns the list of commands that will be executed
	 * 
	 * @return commands
	 */
	List<String> getCommands();
	
	/**
	 * Whether the player should be online or offline
	 * 
	 * @return boolean
	 */
	boolean needToBeOnline();

	/**
	 * Give reward to player
	 * 
	 * @param player
	 */
	void give(Plugin plugin, OfflinePlayer player);
	
	/**
	 * Les messages qui vont §tre envoy§
	 * 
	 * @return messages
	 */
	List<String> getMessages();
	
}
