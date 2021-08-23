package fr.maxlego08.zvoteparty.api;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.zcore.utils.storage.Saveable;

public interface VotePartyManager extends Saveable {

	/**
	 * Allows to reload configuration files
	 * 
	 * @param sender
	 *            - Person who will execute the command
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
	void openVote(Player player);

	/**
	 * Allows you to perform the voting action on a user
	 * 
	 * @param username
	 * @param serviceName
	 */
	void vote(String username, String serviceName);

	/**
	 * Allows you to add one to the voteparty
	 */
	void handleVoteParty();

	/**
	 * Add vote to user
	 * 
	 * @param sender
	 * @param player
	 */
	void vote(CommandSender sender, OfflinePlayer player);

	/**
	 * Add vote to user
	 * 
	 * @param offlinePlayer
	 * @param serviceName
	 */
	void vote(OfflinePlayer offlinePlayer, String serviceName);

	/**
	 * Get reward
	 * 
	 * @return {@link Reward}
	 */
	Reward getRandomReward();

	/**
	 * Allows you to give rewards for pending votes
	 * 
	 * @param player
	 */
	void giveVotes(Player player);
	
	/**
	 * Returns the list of commands for the party vote
	 * 
	 * @return commands
	 */
	List<String> getGlobalCommands();
	
	/**
	 * Return the list of rewards for the voting party
	 * 
	 * @return rewards
	 */
	List<Reward> getPartyReward();
	
	/**
	 * Returns the number of votes needed for the party vote
	 * 
	 * @return needs
	 */
	long getNeedVotes();

	/**
	 * Returns the number of votes of a player
	 * 
	 * @param player
	 * @return votes
	 */
	long getPlayerVoteCount(Player player);

}
