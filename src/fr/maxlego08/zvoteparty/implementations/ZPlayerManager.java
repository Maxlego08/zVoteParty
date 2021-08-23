package fr.maxlego08.zvoteparty.implementations;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.save.Config;
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
			if (player != null)
				persist.save(player, Folder.PLAYERS, player.getFileName());
		});
	}

	@Override
	public void load(Persist persist) {
		// TODO Auto-generated method stub

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
				if (Config.enableDebug)
					e.printStackTrace();
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

}
