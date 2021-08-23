package fr.maxlego08.zvoteparty.implementations;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.zcore.ZPlugin;
import fr.maxlego08.zvoteparty.zcore.enums.Folder;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;

public class ZPlayerManager extends ZUtils implements PlayerManager {

	private final ZPlugin plugin;
	private final Map<UUID, PlayerVote> players = new HashMap<UUID, PlayerVote>();

	public ZPlayerManager(ZPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void save(Persist persist) {
		this.players.forEach((uuid, player) -> {
			persist.save(player, Folder.PLAYERS, player.getFileName());
		});
	}

	@Override
	public void load(Persist persist) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<PlayerVote> getPlayer(OfflinePlayer offlinePlayer) {
		if (this.players.containsKey(offlinePlayer.getUniqueId()))
			return Optional.of(this.players.get(offlinePlayer.getUniqueId()));
		String userFile = Folder.PLAYERS.toFolder() + "/" + offlinePlayer.getName() + ".json";
		File file = new File(plugin.getDataFolder(), userFile);
		if (file.exists()) {
			PlayerVote playerVote = this.plugin.getPersist().loadOrSaveDefault(null, PlayerVote.class, Folder.PLAYERS,
					offlinePlayer.getName());
			players.put(offlinePlayer.getUniqueId(), playerVote);
			return Optional.of(playerVote);
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

}
