package fr.maxlego08.zvoteparty.api;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface PlayerManager {

	/**
	 * 
	 * @param offlinePlayer
	 * @param consumer
	 */
	void getPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer);

	/**
	 * Create new player
	 * 
	 * @param offlinePlayer
	 * @return
	 */
	PlayerVote createPlayer(OfflinePlayer offlinePlayer);

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
