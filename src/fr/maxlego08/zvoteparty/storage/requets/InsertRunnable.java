package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.save.Config;

public class InsertRunnable implements Runnable {

	private final PlayerVote playerVote;
	private final Vote vote;
	private final Reward reward;
	private final IConnection iConnection;

	/**
	 * @param playerVote
	 * @param vote
	 * @param reward
	 * @param iConnection
	 */
	public InsertRunnable(PlayerVote playerVote, Vote vote, Reward reward, IConnection iConnection) {
		super();
		this.playerVote = playerVote;
		this.vote = vote;
		this.reward = reward;
		this.iConnection = iConnection;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.iConnection.getConnection();

			String request = "INSERT INTO zvoteparty_votes (player_uuid, service_name, is_reward_give, reward_percent, commands, need_online, created_at) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(request);

			String commands = "";
			for (int a = 0; a != this.reward.getCommands().size(); a++) {

				commands += this.reward.getCommands().get(a);
				if ((a + 1) < this.reward.getCommands().size()) {
					commands += ";";
				}

			}

			statement.setString(1, this.playerVote.getUniqueId().toString());
			statement.setString(2, this.vote.getServiceName());
			statement.setBoolean(3, this.vote.rewardIsGive());
			statement.setDouble(4, this.reward.getPercent());
			statement.setString(5, commands);
			statement.setBoolean(6, this.reward.needToBeOnline());
			statement.setLong(7, this.vote.getCreatedAt());

			statement.executeUpdate();
			connection.commit();

			statement.close();

		} catch (SQLException e) {
			if (Config.enableDebug) {
				e.printStackTrace();
			}
		}
	}

}
