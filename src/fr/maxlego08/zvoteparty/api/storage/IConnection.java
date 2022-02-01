package fr.maxlego08.zvoteparty.api.storage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;

public interface IConnection {

	/**
	 * 
	 * @return
	 */
	Connection getConnection();

	/*
	 * 
	 */ void asyncConnect();

	/**
	 * 
	 * @throws SQLException
	 */
	void connect() throws SQLException;

	/**
	 * 
	 */
	void disconnect();

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	void getAndRefreshConnection(Runnable runnable);

	/**
	 * 
	 * @param amount
	 */
	void updateVoteCount(long amount);

	/**
	 * 
	 * @param offlinePlayer
	 * @param consumer
	 */
	void asyncFetchPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer);

	/**
	 * 
	 * @param playerVote
	 * @param vote
	 * @param reward
	 */
	void asyncInsert(PlayerVote playerVote, Vote vote, Reward reward);

}
