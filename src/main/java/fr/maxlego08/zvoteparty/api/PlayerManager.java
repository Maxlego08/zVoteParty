package fr.maxlego08.zvoteparty.api;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface PlayerManager {

	/**
	 * Get player using offlineplayer
	 * 
	 * @param offlinePlayer
	 * @param consumer
	 */
	void getPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer, boolean forceDatabaseUpdate);

	/**
	 * Get player using player's UUID
	 * 
	 * @param uuid
	 * @param consumer
	 * @param forceDatabaseUpdate 
	 */
	void getPlayer(UUID uuid, Consumer<Optional<PlayerVote>> consumer, boolean forceDatabaseUpdate);

	/**
	 * Create new player
	 * 
	 * @param offlinePlayer
	 * @return {@link PlayerVote}
	 */
	PlayerVote createPlayer(OfflinePlayer offlinePlayer);

	/**
	 * Create new player
	 * 
	 * @param uuid
	 * @return {@link PlayerVote}
	 */
	PlayerVote createPlayer(UUID uuid);

	/**
	 * Get folder as object
	 * 
	 * @return file
	 */
	File getFolder();

	/**
	 * 
	 * @param player
	 * @return
	 */
	Optional<PlayerVote> getSyncPlayer(Player player);

}
