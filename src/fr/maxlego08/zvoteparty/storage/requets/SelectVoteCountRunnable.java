package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class SelectVoteCountRunnable extends ZUtils implements Runnable {

	private final IConnection iConnection;
	private final IStorage iStorage;
	private int tryAmount = 0;

	/**
	 * @param iConnection
	 * @param iStorage
	 */
	public SelectVoteCountRunnable(IConnection iConnection, IStorage iStorage) {
		super();
		this.iConnection = iConnection;
		this.iStorage = iStorage;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.iConnection.getConnection();

			String request = "SELECT * FROM zvoteparty_count";
			PreparedStatement statement = connection.prepareStatement(request);
			ResultSet resultSet = statement.executeQuery();
			connection.commit();

			resultSet.next();
			this.iStorage.setVoteCount(resultSet.getLong("vote"));

			statement.close();

		} catch (SQLException e) {
			this.tryAmount++;
			if (this.tryAmount < Config.maxSqlRetryAmoun) {
				try {
					this.iConnection.disconnect();
					this.iConnection.connect();
					this.run();
				} catch (SQLException e1) {
					this.iStorage.setVoteCount(0);
					Logger.info("Impossible to use MySQL storage!", LogType.ERROR);
					e1.printStackTrace();
				}
			}
		}
	}

}
