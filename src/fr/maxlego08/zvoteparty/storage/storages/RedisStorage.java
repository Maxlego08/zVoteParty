package fr.maxlego08.zvoteparty.storage.storages;

import java.util.UUID;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.storage.redis.RedisClient;
import fr.maxlego08.zvoteparty.storage.redis.ServerMessaging;
import fr.maxlego08.zvoteparty.zcore.utils.ElapsedTime;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;

public class RedisStorage extends SqlStorage implements IStorage {

	private final RedisClient redisClient;
	private final ServerMessaging messaging;

	/**
	 * @param storage
	 * @param plugin
	 */
	public RedisStorage(Storage storage, ZVotePartyPlugin plugin) {
		super(plugin, storage);
		this.redisClient = new RedisClient();
		this.messaging = new ServerMessaging(plugin, this, this.redisClient);
	}

	@Override
	public void load(Persist persist) {
		super.load(persist);
	}

	@Override
	public void save(Persist persist) {
		super.save(persist);
		this.messaging.stop();
	}

	@Override
	public void performCustomVoteAction(String username, String serviceName, UUID uuid) {
		this.messaging.sendVoteAction(username, serviceName, uuid);
	}

	/**
	 * Add vote count but its a secret
	 * 
	 * @param i
	 */
	public void addSecretVoteCount(int i) {
		this.voteCount += i;
	}

	@Override
	public void addVoteCount(long amount) {
		super.addVoteCount(amount);
		ElapsedTime elapsedTime = new ElapsedTime("Redis message");
		elapsedTime.start();
		this.messaging.sendAddVoteCount();
		elapsedTime.endDisplay();
	}

	@Override
	public void startVoteParty() {
		super.startVoteParty();
		this.messaging.sendHandleVoteParty();
	}

}
