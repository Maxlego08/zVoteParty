package fr.maxlego08.zvoteparty.api.storage;

import java.util.Map;
import java.util.UUID;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;

public interface IStorage extends PlayerManager {

	/**
	 * Get players
	 * 
	 * @return players
	 */
	Map<UUID, PlayerVote> getPlayers();

	/**
	 * 
	 * @return
	 */
	long getVoteCount();

	/**
	 * 
	 * @param amount
	 */
	void addVoteCount(long amount);

	/**
	 * 
	 * @param amount
	 */
	void removeVoteCount(long amount);

	/**
	 * 
	 * @param amount
	 */
	void setVoteCount(long amount);

}
