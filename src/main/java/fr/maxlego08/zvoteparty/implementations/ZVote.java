package fr.maxlego08.zvoteparty.implementations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;

/**
 * Represents a vote with its associated service, creation time, reward, and reward status.
 */
public class ZVote implements Vote {

    private final String serviceName;
    private final long createdAt;
    private final Reward reward;
    private boolean rewardIsGiven;

    /**
     * Constructs a ZVote with the specified parameters.
     *
     * @param serviceName the name of the service related to the vote
     * @param createdAt the timestamp when the vote was created
     * @param reward the reward associated with the vote
     * @param rewardIsGiven whether the reward has been given
     */
    public ZVote(String serviceName, long createdAt, Reward reward, boolean rewardIsGiven) {
        this.serviceName = serviceName;
        this.createdAt = createdAt;
        this.reward = reward;
        this.rewardIsGiven = rewardIsGiven;
    }

    /**
     * Constructs a ZVote with the current timestamp.
     *
     * @param serviceName the name of the service related to the vote
     * @param reward the reward associated with the vote
     * @param rewardIsGiven whether the reward has been given
     */
    public ZVote(String serviceName, Reward reward, boolean rewardIsGiven) {
        this(serviceName, System.currentTimeMillis(), reward, rewardIsGiven);
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public Reward getReward() {
        return reward;
    }

    @Override
    public boolean rewardIsGiven() {
        return rewardIsGiven;
    }

    @Override
    public void giveReward(Plugin plugin, Player player) {
        if (!rewardIsGiven) {
            rewardIsGiven = true;
            reward.give(plugin, player);
        }
    }
}
