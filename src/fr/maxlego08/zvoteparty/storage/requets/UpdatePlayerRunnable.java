package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class UpdatePlayerRunnable extends ZUtils implements Runnable {

	private final IConnection iConnection;
	private final UUID uniqueId;
	private int tryAmount = 0;

	/**
	 * @param connection
	 * @param playerVote
	 */
	public UpdatePlayerRunnable(IConnection connection, UUID uniqueId) {
		super();
		this.iConnection = connection;
		this.uniqueId = uniqueId;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.iConnection.getConnection();

			if (connection == null || connection.isClosed()) {
				this.iConnection.connect();
				connection = this.iConnection.getConnection();
			}

			String request = "UPDATE zvoteparty_votes SET is_reward_give = 1 WHERE player_uuid = ?;";
			PreparedStatement statement = connection.prepareStatement(request);

			statement.setString(1, this.uniqueId.toString());

			statement.executeUpdate();
			connection.commit();

			statement.close();

		} catch (SQLException e) {
			this.tryAmount++;
			if (this.tryAmount < Config.maxSqlRetryAmoun) {
				try {
					this.iConnection.disconnect();
					this.iConnection.connect();
					this.run();
				} catch (SQLException e1) {
					Logger.info("Impossible to use MySQL storage!", LogType.ERROR);
					e1.printStackTrace();
				}
			}
		}
	}

}
