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

/**
 * Implementation of PlayerVote that manages a player's votes.
 */
public class ZPlayerVote extends ZUtils implements PlayerVote {

    private final UUID uniqueId;
    private final List<Vote> votes;

    /**
     * Constructs a ZPlayerVote with the specified uniqueId and an empty list of votes.
     *
     * @param uniqueId the unique identifier of the player
     */
    public ZPlayerVote(UUID uniqueId) {
        this(uniqueId, new ArrayList<>());
    }

    /**
     * Constructs a ZPlayerVote with the specified uniqueId and list of votes.
     *
     * @param uniqueId the unique identifier of the player
     * @param votes the list of votes
     */
    public ZPlayerVote(UUID uniqueId, List<Vote> votes) {
        super();
        this.uniqueId = uniqueId;
        this.votes = votes;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    @Override
    public List<Vote> getVotes() {
        return votes;
    }

    @Override
    public int getVoteCount() {
        return votes.size();
    }

    @Override
    public Vote vote(Plugin plugin, String serviceName, Reward reward, boolean forceStorage) {
        OfflinePlayer offlinePlayer = getPlayer();
        boolean giveReward = false;

        if (!forceStorage) {
            handlePlayerMessages(offlinePlayer);
            
            if (reward.needToBeOnline() && offlinePlayer.isOnline()) {
                giveReward = true;
                reward.give(plugin, offlinePlayer);
            } else if (!reward.needToBeOnline()) {
                giveReward = true;
                reward.give(plugin, offlinePlayer);
            }
        }

        Vote vote = new ZVote(serviceName, reward, giveReward);
        votes.add(vote);
        return vote;
    }

    @Override
    public String getFileName() {
        return uniqueId.toString();
    }

    @Override
    public List<Vote> getNeedRewardVotes() {
        return votes.stream().filter(vote -> !vote.rewardIsGive()).collect(Collectors.toList());
    }

    @Override
    public void removeVote() {
        if (!votes.isEmpty()) {
            votes.remove(0);
        }
    }

    /**
     * Handles sending messages to the player and broadcasting announcements.
     *
     * @param offlinePlayer the player to whom messages will be sent
     */
    private void handlePlayerMessages(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            message(player, Message.VOTE_MESSAGE, "%player%", player.getName());
        }

        if (Config.enableActionBarVoteAnnonce) {
            broadcast(Message.VOTE_BROADCAST_ACTION, "%player%", offlinePlayer.getName());
        }

        if (Config.enableTchatVoteAnnonce) {
            broadcast(Message.VOTE_BROADCAST_TCHAT, "%player%", offlinePlayer.getName());
        }
    }
}
