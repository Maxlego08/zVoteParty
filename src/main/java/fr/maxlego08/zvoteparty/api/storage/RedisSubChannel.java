package fr.maxlego08.zvoteparty.api.storage;

public enum RedisSubChannel {

	HANDLE_VOTEPARTY, ADD_VOTEPARTY, ADD_VOTE, VOTE_RESPONSE, OTHER,

	;

	/**
	 * Get channel by name if no channel is found, then the other channel will be sent.
	 * 
	 * @param name of channel
	 * @return {@link RedisSubChannel}
	 */
	public static RedisSubChannel byName(String name) {
		for (RedisSubChannel channel : RedisSubChannel.values()) {
			if (channel.name().equalsIgnoreCase(name)) {
				return channel;
			}
		}
		return RedisSubChannel.OTHER;
	}

}
