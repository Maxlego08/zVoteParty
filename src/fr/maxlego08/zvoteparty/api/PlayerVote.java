package fr.maxlego08.zvoteparty.api;

import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

public interface PlayerVote {

	/**
	 * Returns the unique identifier of the player
	 * 
	 * @return {@link UUID}
	 */
	UUID getUniqueId();
	
	/**
	 * Returns the player as an offline player
	 * 
	 * @return {@link OfflinePlayer}
	 */
	OfflinePlayer getPlayer();
	
	/**
	 * Returns the player's vote list
	 * 
	 * @return votes
	 */
	List<Vote> getVotes();
	

	/**
	 * Returns the number of votes of the player
	 * 
	 * @return votes amount
	 */
	int getVoteCount();
	
	/**
	 * Allows the player to vote
	 * 
	 * @param link - Website link
	 */
	void vote(String link, Reward reward);
	
}
