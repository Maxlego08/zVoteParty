package fr.maxlego08.zvoteparty.api;

public interface Vote {

	/**
	 * Return the link of the voting site
	 * 
	 * @return link
	 */
	String getLink();
	
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
	
}
