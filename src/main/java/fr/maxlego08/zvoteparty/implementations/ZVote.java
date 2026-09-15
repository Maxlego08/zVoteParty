package fr.maxlego08.zvoteparty.implementations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;

public class ZVote implements Vote {

    private final String serviceName;
    private final long createdAt;
    private final Reward reward;
    private boolean rewardIsGiven;

    public ZVote(String serviceName, long createdAt, Reward reward, boolean rewardIsGiven) {
        this.serviceName = serviceName;
        this.createdAt = createdAt;
        this.reward = reward;
        this.rewardIsGiven = rewardIsGiven;
    }

    public ZVote(String serviceName, Reward reward, boolean rewardIsGiven) {
        this(serviceName, System.currentTimeMillis(), reward, rewardIsGiven);
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
        return this.rewardIsGiven;
    }

    @Override
    public void giveReward(Plugin plugin, Player player) {
        if (player == null || reward == null) return;
        try {
            this.rewardIsGiven = true;
            reward.give(plugin, player);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to give reward to " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
