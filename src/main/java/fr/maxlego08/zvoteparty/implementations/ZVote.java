package fr.maxlego08.zvoteparty.implementations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;

public class ZVote implements Vote {

	private final String serviceName;
	private final long createdAt;
	private final Reward reward;
	private boolean rewardIsGive;

	/**
	 * @param serviceName
	 * @param createdAt
	 * @param reward
	 * @param rewardIsGive
	 */
	public ZVote(String serviceName, long createdAt, Reward reward, boolean rewardIsGive) {
		super();
		this.serviceName = serviceName;
		this.createdAt = createdAt;
		this.reward = reward;
		this.rewardIsGive = rewardIsGive;
	}

	/**
	 * @param link
	 */
	public ZVote(String serviceName, Reward reward, boolean rewardIsGive) {
		this(serviceName, System.currentTimeMillis(), reward, rewardIsGive);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public long getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public Reward getReward() {
		return this.reward;
	}

	@Override
	public boolean rewardIsGive() {
		return this.rewardIsGive;
	}

	@Override
	public void giveReward(Plugin plugin, Player player) {
		this.rewardIsGive = true;
		this.reward.give(plugin, player);
	}

}
