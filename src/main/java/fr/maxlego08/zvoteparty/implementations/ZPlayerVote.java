package fr.maxlego08.zvoteparty.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZPlayerVote extends ZUtils implements PlayerVote {

    private final UUID uniqueId;
    private final List<Vote> votes;

    public ZPlayerVote(UUID uniqueId) {
        this(uniqueId, new ArrayList<>());
    }

    public ZPlayerVote(UUID uniqueId, List<Vote> votes) {
        this.uniqueId = uniqueId;
        this.votes = votes != null ? votes : new ArrayList<>();
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.uniqueId);
    }

    @Override
    public List<Vote> getVotes() {
        return this.votes;
    }

    @Override
    public int getVoteCount() {
        return this.votes.size();
    }

    @Override
    public Vote vote(Plugin plugin, String serviceName, Reward reward, boolean forceStorage) {
        if (plugin == null || reward == null || serviceName == null) return null;

        OfflinePlayer offlinePlayer = getPlayer();
        boolean give = false;

        if (!forceStorage) {
            try {
                if (offlinePlayer.isOnline()) {
                    Player player = offlinePlayer.getPlayer();
                    message(player, Message.VOTE_MESSAGE, "%player%", player.getName());
                }

                if (Config.enableActionBarVoteAnnonce) broadcast(Message.VOTE_BROADCAST_ACTION, "%player%", offlinePlayer.getName());
                if (Config.enableTchatVoteAnnonce) broadcast(Message.VOTE_BROADCAST_TCHAT, "%player%", offlinePlayer.getName());

                if (reward.needToBeOnline()) {
                    if (offlinePlayer.isOnline()) {
                        give = true;
                        reward.give(plugin, offlinePlayer);
                    }
                } else {
                    give = true;
                    reward.give(plugin, offlinePlayer);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to process vote for " + offlinePlayer.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        Vote vote = new ZVote(serviceName, reward, give);
        this.votes.add(vote);
        return vote;
    }

    @Override
    public String getFileName() {
        return this.uniqueId.toString();
    }

    @Override
    public List<Vote> getNeedRewardVotes() {
        return this.votes.stream().filter(v -> !v.rewardIsGive()).collect(Collectors.toList());
    }

    @Override
    public void removeVote() {
        if (!votes.isEmpty()) votes.remove(0);
    }
}