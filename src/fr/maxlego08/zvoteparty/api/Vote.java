package fr.maxlego08.zvoteparty.api;

public interface Vote {

	/**
	 * Return the service name of the voting site
	 * 
	 * @return serviceName
	 */
	String getServiceName();

	/**
	 * Returns the date the vote was created
	 * 
	 * @return created at
	 */
	long getCreatedAt();

	/**
	 * Returns the reward that the player got
	 * 
	 * @return {@link Reward}
	 */
	Reward getReward();

	/**
	 * The reward has already been given
	 * 
	 * @return boolean
	 */
	boolean rewardIsGive();

}
