package fr.maxlego08.zvoteparty.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZPlayerVote extends ZUtils implements PlayerVote {

	private final UUID uniqueId;
	private final List<Vote> votes;

	/**
	 * @param uniqueId
	 * @param votes
	 */
	public ZPlayerVote(UUID uniqueId) {
		this(uniqueId, new ArrayList<>());
	}

	/**
	 * @param uniqueId
	 * @param votes
	 */
	public ZPlayerVote(UUID uniqueId, List<Vote> votes) {
		super();
		this.uniqueId = uniqueId;
		this.votes = votes;
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
	public void vote(String serviceName, Reward reward) {
		OfflinePlayer offlinePlayer = this.getPlayer();
		if (offlinePlayer.isOnline()) {
			Player player = offlinePlayer.getPlayer();

			if (Config.enableActionBarVoteAnnonce)
				broadcast(Message.VOTE_BROADCAST_ACTION, "%player%", player.getName());

			if (Config.enableTchatVoteAnnonce)
				broadcastTchat(Message.VOTE_BROADCAST_TCHAT, "%player%", player.getName());

			message(player, Message.VOTE_MESSAGE, "%player%", player.getName());
		}

		boolean give = false;
		if (reward.needToBeOnline()) {
			if (offlinePlayer.isOnline()) {
				give = true;
				reward.give(offlinePlayer.getPlayer());
			}
		} else {
			give = true;
			reward.give(offlinePlayer.getPlayer());
		}

		Vote vote = new ZVote(serviceName, reward, give);
		this.votes.add(vote);
	}

	@Override
	public String getFileName() {
		return this.uniqueId.toString();
	}

	@Override
	public List<Vote> getNeedRewardVotes() {
		return this.votes.stream().filter(v -> !v.rewardIsGive()).collect(Collectors.toList());
	}

}
