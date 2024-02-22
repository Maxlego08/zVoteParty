package fr.maxlego08.zvoteparty.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Vote {

	/**
	 * Return the service name of the voting site
	 * 
	 * @return serviceName
	 */
	String getServiceName();

	/**
	 * Returns the date the vote was created
	 * 
	 * @return created at
	 */
	long getCreatedAt();

	/**
	 * Returns the reward that the player got
	 * 
	 * @return {@link Reward}
	 */
	Reward getReward();

	/**
	 * The reward has already been given
	 * 
	 * @return boolean
	 */
	boolean rewardIsGive();

	/**
	 * Gives the reward to the player
	 * 
	 * @param player
	 */
	void giveReward(Plugin plugin, Player player);

}
