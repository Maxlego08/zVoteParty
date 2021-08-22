package fr.maxlego08.zvoteparty.implementations;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;

public class ZVote implements Vote {

	private final String link;
	private final long createdAt;
	private final Reward reward;
	
	/**
	 * @param link
	 */
	public ZVote(String link, Reward reward) {
		super();
		this.link = link;
		this.createdAt = System.currentTimeMillis();
		this.reward = reward;
	}

	@Override
	public String getLink() {
		return this.link;
	}

	@Override
	public long getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public Reward getReward() {
		return this.reward;
	}

}
