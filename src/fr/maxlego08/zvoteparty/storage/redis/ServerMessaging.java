package fr.maxlego08.zvoteparty.storage.redis;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.storage.storages.RedisStorage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ServerMessaging extends JedisPubSub {

	private final String CHANNEL_VOTEPARTY = "zvoteparty:voteparty";

	private final ZVotePartyPlugin plugin;
	private final RedisStorage storage;
	private final RedisClient client;

	private Thread threadMessaging1;

	/**
	 * @param plugin
	 * @param storage
	 * @param client
	 */
	public ServerMessaging(ZVotePartyPlugin plugin, RedisStorage storage, RedisClient client) {
		super();
		this.plugin = plugin;
		this.storage = storage;
		this.client = client;

		this.threadMessaging1 = new Thread(() -> {
			try (Jedis jedis = client.getPool()) {
				jedis.subscribe(this, this.CHANNEL_VOTEPARTY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		this.threadMessaging1.start();
	}

	@Override
	public void onMessage(String channel, String message) {

		if (!this.plugin.isEnabled()) {
			System.out.println("je devrais pas être ici :x");
			return;
		}

		try {

			System.out.println(" ici ! " + channel);
			if (channel.equals(this.CHANNEL_VOTEPARTY)) {
				this.storage.addSecretVoteCount(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendAddVoteCount() {
		try (Jedis jedis = this.client.getPool()) {
			jedis.publish(this.CHANNEL_VOTEPARTY, "Update vote count");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		this.threadMessaging1.interrupt();
		this.unsubscribe(this.CHANNEL_VOTEPARTY);
	}

}
