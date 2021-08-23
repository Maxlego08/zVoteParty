package fr.maxlego08.zvoteparty.api;

import java.util.List;

import org.bukkit.entity.Player;

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
	void give(Player player);
	
}
