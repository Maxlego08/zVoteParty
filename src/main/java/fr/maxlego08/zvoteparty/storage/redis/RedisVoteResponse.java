package fr.maxlego08.zvoteparty.storage.redis;

import java.util.UUID;

public class RedisVoteResponse {

	private final UUID messageId;
	private final String username;
	private final String serviceName;
	private int responseCount;
	private UUID userId;

	/**
	 * @param messageId
	 * @param username
	 * @param serviceName
	 * @param responseCount
	 * @param userId
	 */
	public RedisVoteResponse(UUID messageId, String username, String serviceName, int responseCount, UUID userId) {
		super();
		this.messageId = messageId;
		this.username = username;
		this.serviceName = serviceName;
		this.responseCount = responseCount;
		this.userId = userId;
	}

	/**
	 * @return the messageId
	 */
	public UUID getMessageId() {
		return messageId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the responseCount
	 */
	public int getResponseCount() {
		return responseCount;
	}

	/**
	 * @return the userId
	 */
	public UUID getUserId() {
		return userId;
	}

	/**
	 * @param responseCount
	 *            the responseCount to set
	 */
	public void setResponseCount(int responseCount) {
		this.responseCount = responseCount;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public void addResponse(String userId) {
		this.responseCount += 1;
		if (this.userId == null && userId != null && userId.length() == 36) {
			this.userId = UUID.fromString(userId);
		}
	}

}
