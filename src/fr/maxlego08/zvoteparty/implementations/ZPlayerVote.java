package fr.maxlego08.zvoteparty.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;

public class ZPlayerVote implements PlayerVote {

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
	public void vote(String link, Reward reward) {
		OfflinePlayer offlinePlayer = this.getPlayer();
		if (offlinePlayer.isOnline()) {
			Player player = offlinePlayer.getPlayer();
			// to do
			System.out.println(player.getName() + " - vote to do");
		}

		Vote vote = new ZVote(link, reward);
		this.votes.add(vote);
	}

}
