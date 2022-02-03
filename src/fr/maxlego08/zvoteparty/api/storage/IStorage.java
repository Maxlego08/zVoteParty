package fr.maxlego08.zvoteparty.api.storage;

import java.util.Map;
import java.util.UUID;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Saveable;

public interface IStorage extends PlayerManager, Saveable {

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

	/**
	 * Add vote to datebase
	 * 
	 * @param playerVote
	 * @param vote
	 * @param reward
	 */
	void insertVote(PlayerVote playerVote, Vote vote, Reward reward);

	/**
	 * Usefull for redis
	 * 
	 * @param username
	 * @param serviceName
	 * @param uuid 
	 */
	void performCustomVoteAction(String username, String serviceName, UUID uuid);

	/**
	 * Start vote party
	 */
	void startVoteParty();

}
