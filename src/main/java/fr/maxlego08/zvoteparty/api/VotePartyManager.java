package fr.maxlego08.zvoteparty.api;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.enums.RewardType;
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
	void vote(String username, String serviceName, boolean updateVoteParty);

	/**
	 * Allows you to add one to the voteparty
	 */
	void handleVoteParty();

	/**
	 * Add vote to user
	 * 
	 * @param sender
	 * @param username
	 * @param updateVoteParty
	 */
	void vote(CommandSender sender, String username, boolean updateVoteParty);

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
	 * @param type
	 * @return {@link Reward}
	 */
	Reward getRandomReward(RewardType type);

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

	/**
	 * Send need vote to sender
	 * 
	 * @param sender
	 */
	void sendNeedVote(CommandSender sender);

	/**
	 * Force start vote party
	 * 
	 * @param sender
	 */
	void forceStart(CommandSender sender);

	/**
	 * Start vote party
	 */
	void start();

	/**
	 * 
	 * @param sender
	 * @param player
	 */
	void removeVote(CommandSender sender, OfflinePlayer player);

	/**
	 * Secret start	 
	 */
	void secretStart();

	/**
	 * Secret vote
	 *  
	 * @param username Nickname of the user who voted
	 * @param serviceName Name of the service where the player voted
	 */
	boolean secretVote(String username, String serviceName);

	/**
	 * Allows you to add a vote in the database
	 * 
	 * @param userId Player's UUID
	 * @param serviceName Name of the service where the player voted
	 */
	void voteOffline(UUID userId, String serviceName);

}
