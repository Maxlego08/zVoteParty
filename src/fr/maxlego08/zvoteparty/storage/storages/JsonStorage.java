package fr.maxlego08.zvoteparty.storage.storages;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.implementations.ZPlayerVote;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.ZPlugin;
import fr.maxlego08.zvoteparty.zcore.enums.Folder;

public class JsonStorage implements IStorage {

	private transient final ZPlugin plugin;
	private transient final Map<UUID, PlayerVote> players = new HashMap<UUID, PlayerVote>();

	private long voteCount;

	/**
	 * @param plugin
	 */
	public JsonStorage(ZPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public Optional<PlayerVote> getPlayer(OfflinePlayer offlinePlayer) {

		UUID uniqueId = offlinePlayer.getUniqueId();

		if (this.players.containsKey(uniqueId))
			return Optional.of(this.players.get(uniqueId));

		String userFile = Folder.PLAYERS.toFolder() + "/" + uniqueId + ".json";
		File file = new File(plugin.getDataFolder(), userFile);
		if (file.exists()) {
			try {
				PlayerVote playerVote = this.plugin.getPersist().loadOrSaveDefault(null, ZPlayerVote.class,
						Folder.PLAYERS, uniqueId.toString());
				players.put(uniqueId, playerVote);
				return Optional.of(playerVote);
			} catch (Exception e) {
				if (Config.enableDebug) {
					e.printStackTrace();
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public PlayerVote createPlayer(OfflinePlayer offlinePlayer) {
		PlayerVote playerVote = new ZPlayerVote(offlinePlayer.getUniqueId());
		players.put(offlinePlayer.getUniqueId(), playerVote);
		this.plugin.getPersist().save(playerVote, Folder.PLAYERS, playerVote.getFileName());
		return playerVote;
	}

	@Override
	public File getFolder() {
		return new File(this.plugin.getDataFolder(), Folder.PLAYERS.toFolder());
	}

	@Override
	public Map<UUID, PlayerVote> getPlayers() {
		return this.players;
	}

	@Override
	public long getVoteCount() {
		return this.voteCount;
	}

	@Override
	public void addVoteCount(long amount) {
		this.voteCount += amount;
	}

	@Override
	public void removeVoteCount(long amount) {
		this.voteCount -= amount;
	}

	@Override
	public void setVoteCount(long amount) {
		this.voteCount = amount;
	}

}
