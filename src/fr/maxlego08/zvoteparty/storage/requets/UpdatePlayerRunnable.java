package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class UpdatePlayerRunnable extends ZUtils implements Runnable {

	private final IConnection connection;
	private final UUID uniqueId;

	/**
	 * @param connection
	 * @param playerVote
	 */
	public UpdatePlayerRunnable(IConnection connection, UUID uniqueId) {
		super();
		this.connection = connection;
		this.uniqueId = uniqueId;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.connection.getConnection();

			if (connection == null || connection.isClosed()) {
				this.connection.connect();
				connection = this.connection.getConnection();
			}

			String request = "UPDATE zvoteparty_votes SET is_reward_give = 1 WHERE player_uuid = ?;";
			PreparedStatement statement = connection.prepareStatement(request);

			statement.setString(1, this.uniqueId.toString());

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
