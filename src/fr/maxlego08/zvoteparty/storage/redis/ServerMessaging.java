package fr.maxlego08.zvoteparty.storage.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.storage.RedisSubChannel;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.storage.storages.RedisStorage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ServerMessaging extends JedisPubSub {

	private final ZVotePartyPlugin plugin;
	private final RedisStorage storage;
	private final RedisClient client;

	private Thread threadMessaging1;

	private List<UUID> sendingUUID = new ArrayList<UUID>();

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
				jedis.subscribe(this, Config.redisChannel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		this.threadMessaging1.start();
	}

	@Override
	public void onMessage(String channel, String message) {

		if (!this.plugin.isEnabled()) {
			return;
		}

		try {

			if (channel.equals(Config.redisChannel)) {

				String[] values = message.split(";;");

				System.out.println(Arrays.asList(values));

				RedisSubChannel subChannel = RedisSubChannel.byName(values[0]);
				String uuidAsString = values[1];
				UUID uuid = UUID.fromString(uuidAsString);

				if (this.sendingUUID.contains(uuid)) {
					return;
				}

				switch (subChannel) {
				case ADD_VOTEPARTY:
					this.storage.addSecretVoteCount(1);
					break;
				case HANDLE_VOTEPARTY:
					this.plugin.getManager().secretStart();
					break;
				case ADD_VOTE:
					String username = values[2];
					String serviceName = values[3];
					this.plugin.getManager().secretVote(username, serviceName);
					break;
				default:
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'envoyer un message
	 * 
	 * @param subChannel
	 * @param message
	 */
	private void sendMessage(RedisSubChannel channel, String message) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			try (Jedis jedis = this.client.getPool()) {
				UUID uuid = UUID.randomUUID();
				this.sendingUUID.add(uuid);

				String jMessage = channel.name() + ";;" + uuid.toString();
				if (message != null) {
					jMessage += ";;" + message;
				}

				jedis.publish(Config.redisChannel, jMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Permet d'arrêter le thread
	 */
	public void stop() {
		this.threadMessaging1.interrupt();
		this.unsubscribe(Config.redisChannel);
	}

	/**
	 * Permet d'envoyer un message
	 * 
	 * @param subChannel
	 */
	private void sendMessage(RedisSubChannel channel) {
		this.sendMessage(channel, null);
	}

	/**
	 * Permet d'ajouter un vote
	 */
	public void sendAddVoteCount() {
		this.sendMessage(RedisSubChannel.ADD_VOTEPARTY);
	}

	/**
	 * Permet d'envoyer l'information pour lancer le vote party
	 */
	public void sendHandleVoteParty() {
		this.sendMessage(RedisSubChannel.HANDLE_VOTEPARTY);
	}

	public void sendVoteAction(String username, String serviceName) {

		String message = username + ";;" + serviceName;
		this.sendMessage(RedisSubChannel.ADD_VOTE, message);

	}

}
