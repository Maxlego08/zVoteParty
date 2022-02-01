package fr.maxlego08.zvoteparty.storage;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.api.storage.StorageManager;
import fr.maxlego08.zvoteparty.save.VoteStorage;
import fr.maxlego08.zvoteparty.storage.storages.JsonStorage;
import fr.maxlego08.zvoteparty.zcore.enums.Folder;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;

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

		default:
			break;
		}
	}

	@Override
	public void load(Persist persist) {
		switch (this.storage) {
		case JSON:
			System.out.println(VoteStorage.voteCount);
			VoteStorage.getInstance().load(this.plugin.getPersist());
			this.iStorage.setVoteCount(VoteStorage.voteCount);
			System.out.println(VoteStorage.voteCount);		
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
