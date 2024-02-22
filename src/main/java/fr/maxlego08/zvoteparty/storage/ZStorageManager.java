package fr.maxlego08.zvoteparty.storage;

import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.save.VoteStorage;
import fr.maxlego08.zvoteparty.storage.storages.JsonStorage;
import fr.maxlego08.zvoteparty.storage.storages.RedisStorage;
import fr.maxlego08.zvoteparty.storage.storages.SqlStorage;
import fr.maxlego08.zvoteparty.zcore.enums.Folder;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;
import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.api.storage.StorageManager;

public class ZStorageManager implements StorageManager {

	private final Storage storage;
	private final ZVotePartyPlugin plugin;
	private IStorage iStorage;

	/**
	 * @param storage
	 * @param plugin
	 */
	public ZStorageManager(Storage storage, ZVotePartyPlugin plugin) {
		super();
		this.storage = storage;
		this.plugin = plugin;

		switch (storage) {
		case JSON:
			this.iStorage = new JsonStorage(plugin);
			break;
		case MYSQL:
		case SQLITE:
		case PGSQL:
		case MARIADB:
			this.iStorage = new SqlStorage(plugin, storage);
			break;
		case REDIS:
			this.iStorage = new RedisStorage(Config.redisSqlStorage, plugin);
			break;
		default:
			break;
		}
	}

	@Override
	public void save(Persist persist) {
		switch (this.storage) {
		case JSON:
			VoteStorage.voteCount = this.iStorage.getVoteCount();
			VoteStorage.getInstance().save(this.plugin.getPersist());
			this.iStorage.getPlayers().forEach((uuid, player) -> {
				if (player != null) {
					persist.save(player, Folder.PLAYERS, player.getFileName());
				}
			});
			break;
		case MYSQL:
		case SQLITE:
		case PGSQL:
		case MARIADB:
		case REDIS:
			this.iStorage.save(persist);
			break;
		default:
			break;
		}
	}

	@Override
	public void load(Persist persist) {
		switch (this.storage) {
		case JSON:
			VoteStorage.getInstance().load(this.plugin.getPersist());
			this.iStorage.setVoteCount(VoteStorage.voteCount);
		case MYSQL:
		case SQLITE:
		case PGSQL:
		case MARIADB:
		case REDIS:
			this.iStorage.load(persist);
			break;
		default:
			break;
		}
	}

	@Override
	public Storage getStorage() {
		return this.storage;
	}

	@Override
	public IStorage getIStorage() {
		return this.iStorage;
	}

}
