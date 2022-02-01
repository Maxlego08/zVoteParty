package fr.maxlego08.zvoteparty.api;

import java.io.File;
import java.util.Optional;

import org.bukkit.OfflinePlayer;

public interface PlayerManager{

	/**
	 * Return {@link PlayerVote} using {@link OfflinePlayer}
	 * 
	 * @param offlinePlayer
	 * @return PlayerVote
	 */
	Optional<PlayerVote> getPlayer(OfflinePlayer offlinePlayer);
	
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
	
}
