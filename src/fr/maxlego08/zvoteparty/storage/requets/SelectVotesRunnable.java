package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.implementations.ZPlayerVote;
import fr.maxlego08.zvoteparty.implementations.ZReward;
import fr.maxlego08.zvoteparty.implementations.ZVote;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class SelectVotesRunnable extends ZUtils implements Runnable {

	private final IConnection iConnection;
	private final UUID uniqueId;
	private final Consumer<Optional<PlayerVote>> consumer;

	/**
	 * @param storage
	 * @param iConnection
	 * @param uniqueId
	 * @param consumer
	 */
	public SelectVotesRunnable(IConnection iConnection, UUID uniqueId, Consumer<Optional<PlayerVote>> consumer) {
		super();
		this.iConnection = iConnection;
		this.uniqueId = uniqueId;
		this.consumer = consumer;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.iConnection.getConnection();

			List<Vote> votes = new ArrayList<>();

			String request = "SELECT * FROM zvoteparty_votes WHERE player_uuid = ?";
			PreparedStatement statement = connection.prepareStatement(request);
			statement.setString(1, this.uniqueId.toString());
			ResultSet resultSet = statement.executeQuery();
			connection.commit();

			while (resultSet.next()) {

				String serviceName = resultSet.getString("service_name");
				boolean isRewardGive = resultSet.getBoolean("is_reward_give");
				double rewardPercent = resultSet.getDouble("reward_percent");
				String commandsAsString = resultSet.getString("commands");
				boolean needOnline = resultSet.getBoolean("need_online");
				long createdAt = resultSet.getLong("created_at");

				List<String> commands = Arrays.asList(commandsAsString.split(";"));
				Reward reward = new ZReward(rewardPercent, commands, needOnline);
				Vote vote = new ZVote(serviceName, createdAt, reward, isRewardGive);
				votes.add(vote);

			}

			statement.close();
			PlayerVote playerVote = new ZPlayerVote(this.uniqueId, votes);
			this.consumer.accept(Optional.of(playerVote));

		} catch (SQLException e) {
			this.consumer.accept(Optional.empty());
		}
	}

}
