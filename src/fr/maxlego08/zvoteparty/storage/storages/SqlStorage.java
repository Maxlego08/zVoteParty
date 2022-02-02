package fr.maxlego08.zvoteparty.storage.storages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.Script;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.implementations.ZPlayerVote;
import fr.maxlego08.zvoteparty.storage.utils.ScriptRunner;
import fr.maxlego08.zvoteparty.storage.utils.ZConnection;
import fr.maxlego08.zvoteparty.zcore.enums.Folder;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;

public class SqlStorage extends ZUtils implements IStorage {

	private final ZVotePartyPlugin plugin;
	private final Storage storage;

	private IConnection iConnection;

	private transient final Map<UUID, PlayerVote> players = new HashMap<UUID, PlayerVote>();
	private long voteCount = 1;

	/**
	 * @param plugin
	 * @param storage
	 */
	public SqlStorage(ZVotePartyPlugin plugin, Storage storage) {
		super();
		this.plugin = plugin;
		this.storage = storage;
	}

	@Override
	public void load(Persist persist) {

		Logger.info("Load SQL...");
		String user = plugin.getConfig().getString("sql.user");
		String password = plugin.getConfig().getString("sql.password");
		String host = plugin.getConfig().getString("sql.host");
		String dataBase = plugin.getConfig().getString("sql.database");
		int port = plugin.getConfig().getInt("sql.port");
		this.iConnection = new ZConnection(storage, user, password, host, dataBase, port);

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			try {
				this.iConnection.connect();
				Logger.info("Database connect to " + host);
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}

			try {

				for (Script script : Script.values()) {
					File file = new File(plugin.getDataFolder(), "scripts/" + script.name().toLowerCase() + ".sql");
					ScriptRunner runner = new ScriptRunner(iConnection.getConnection());
					Reader reader = new BufferedReader(new FileReader(file));
					runner.runScript(reader);
					Logger.info("Script " + script.name() + " successfuly run", LogType.SUCCESS);
					reader.close();
				}

				this.iConnection.fetchVotes(this);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
		});

	}

	@Override
	public void save(Persist persist) {
		// TODO Auto-generated method stub

	}

	@Override
	public PlayerVote createPlayer(OfflinePlayer offlinePlayer) {
		PlayerVote playerVote = new ZPlayerVote(offlinePlayer.getUniqueId());
		players.put(offlinePlayer.getUniqueId(), playerVote);
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
		this.iConnection.updateVoteCount(this.voteCount);
	}

	@Override
	public void removeVoteCount(long amount) {
		this.voteCount -= amount;
		this.iConnection.updateVoteCount(this.voteCount);
	}

	@Override
	public void setVoteCount(long amount) {
		this.voteCount = amount;
		this.iConnection.updateVoteCount(this.voteCount);
	}

	@Override
	public void getPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer) {
		if (this.players.containsKey(offlinePlayer.getUniqueId())) {
			consumer.accept(Optional.of(this.players.get(offlinePlayer.getUniqueId())));
		} else {
			this.iConnection.asyncFetchPlayer(offlinePlayer, consumer);
		}
	}

	@Override
	public Optional<PlayerVote> getSyncPlayer(Player player) {
		if (this.players.containsKey(player.getUniqueId())) {
			return Optional.of(this.players.get(player.getUniqueId()));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void insertVote(PlayerVote playerVote, Vote vote, Reward reward) {
		this.iConnection.asyncInsert(playerVote, vote, reward);
	}

}
