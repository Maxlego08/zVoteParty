package fr.maxlego08.zvoteparty.storage.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.storage.RedisSubChannel;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.storage.storages.RedisStorage;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ServerMessaging extends JedisPubSub {

	private final String SEPARATOR = ";;";

	private final ZVotePartyPlugin plugin;
	private final RedisStorage storage;
	private final RedisClient client;

	private Thread threadMessaging1;

	private List<UUID> sendingUUID = new ArrayList<UUID>();

	private Map<UUID, RedisVoteResponse> voteResponses = new HashMap<UUID, RedisVoteResponse>();

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

				String[] values = message.split(this.SEPARATOR);

				RedisSubChannel subChannel = RedisSubChannel.byName(values[0]);
				String uuidAsString = values[1];
				UUID uuid = UUID.fromString(uuidAsString);

				// Allows to verify that the server sending the information does
				// not receive it.

				if (this.sendingUUID.contains(uuid)) {
					this.sendingUUID.remove(uuid);
					return;
				}

				switch (subChannel) {
				case ADD_VOTEPARTY:
					this.storage.addSecretVoteCount(1);
					break;
				case HANDLE_VOTEPARTY:
					this.storage.setSecretVoteCount(0);
					this.plugin.getManager().secretStart();
					break;
				case ADD_VOTE:
					String username = values[2];
					String serviceName = values[3];
					if (!this.plugin.getManager().secretVote(username, serviceName)) {
						this.handleVoteResponseError(uuid, username, serviceName);
					} else {
						this.handleVoteResponse(uuid, true, null);
					}
					break;
				case VOTE_RESPONSE:
					UUID messageId = UUID.fromString(values[2]);
					boolean isSuccess = Boolean.valueOf(values[3]);
					String userId = values[4];
					this.processResponse(messageId, isSuccess, userId);
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
	 * Allows you to send a message
	 * 
	 * @param subChannel
	 * @param message
	 */
	private UUID sendMessage(RedisSubChannel channel, String message) {
		final UUID uuid = UUID.randomUUID();
		ZVotePartyPlugin.getScheduler().runAsync(task -> {
			try (Jedis jedis = this.client.getPool()) {
				this.sendingUUID.add(uuid);

				String jMessage = channel.name() + this.SEPARATOR + uuid.toString();
				if (message != null) {
					jMessage += this.SEPARATOR + message;
				}

				jedis.publish(Config.redisChannel, jMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return uuid;
	}

	/**
	 * Allows to stop the thread
	 */
	public void stop() {
		this.threadMessaging1.interrupt();
		this.unsubscribe(Config.redisChannel);
	}

	/**
	 * Allows you to send a message
	 * 
	 * @param subChannel
	 */
	private UUID sendMessage(RedisSubChannel channel) {
		return this.sendMessage(channel, null);
	}

	/**
	 * Allows you to add a vote to the voteparty
	 */
	public void sendAddVoteCount() {
		this.sendMessage(RedisSubChannel.ADD_VOTEPARTY);
	}

	/**
	 * Allows you to send the information to start the voting party
	 */
	public void sendHandleVoteParty() {
		this.sendMessage(RedisSubChannel.HANDLE_VOTEPARTY);
	}

	/**
	 * Allows you to send the voting action
	 * 
	 * @param username
	 * @param serviceName
	 * @param uuid
	 */
	public void sendVoteAction(String username, String serviceName, UUID uuid) {

		String message = username + ";;" + serviceName;
		UUID messageId = this.sendMessage(RedisSubChannel.ADD_VOTE, message);

		// Allows to give the reward if the player is not connected
		RedisVoteResponse redisVoteResponse = new RedisVoteResponse(messageId, username, serviceName, 1, uuid);
		this.voteResponses.put(messageId, redisVoteResponse);

	}

	@SuppressWarnings("deprecation")
	/**
	 * Allows you to reply to the server that sent the voting request to say
	 * that the player is not allowed to vote
	 * 
	 * @param uuid
	 * @param username
	 * @param serviceName
	 */
	private void handleVoteResponseError(UUID uuid, String username, String serviceName) {

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
		this.handleVoteResponse(uuid, false, offlinePlayer != null ? offlinePlayer.getUniqueId().toString() : null);

	}

	/**
	 * We will return the answer with all the information
	 * 
	 * @param uuid
	 * @param isSuccess
	 * @param message
	 */
	private void handleVoteResponse(UUID uuid, boolean isSuccess, String message) {

		String jMessage = uuid.toString() + this.SEPARATOR + String.valueOf(isSuccess) + this.SEPARATOR + message;
		this.sendMessage(RedisSubChannel.VOTE_RESPONSE, jMessage);

	}

	/**
	 * Allows you to perform an action when receiving the voting confirmation
	 * 
	 * @param messageId
	 *            Identifier of the message that sent the request to vote
	 * @param isSuccess
	 *            Allows to know if the vote is successful
	 * @param userId
	 *            Player's UUID
	 */
	private void processResponse(UUID messageId, boolean isSuccess, String userId) {

		RedisVoteResponse redisVoteResponse = this.voteResponses.getOrDefault(messageId, null);

		// If the redis vote response is null, then the messageID is incorrect
		// or the value is delete
		if (redisVoteResponse == null) {
			return;
		}

		// If the answer is a success, then we delete the value
		if (isSuccess) {
			this.voteResponses.remove(messageId);
		} else {

			// Otherwise we check that the number of responses corresponds to
			// the number of servers indicated in the configuration file
			// We also add the UUID of the player if it is present

			redisVoteResponse.addResponse(userId);

			if (redisVoteResponse.getResponseCount() >= Config.redisServerAmount) {

				// We will check if the UUID of the player exists, if yes then
				// we will give a reward so that the player can recover it when
				// he will connect

				// If the player cannot be found, then nothing can be done and
				// the vote will be lost

				if (redisVoteResponse.getUserId() != null) {

					this.plugin.getManager().voteOffline(redisVoteResponse.getUserId(),
							redisVoteResponse.getServiceName());

				} else {

					Logger.info("Impossible to find the player " + redisVoteResponse.getUsername(), LogType.WARNING);

				}

			}

		}

	}

}
