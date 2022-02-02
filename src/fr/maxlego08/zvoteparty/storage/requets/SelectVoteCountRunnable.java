package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class SelectVoteCountRunnable extends ZUtils implements Runnable {

	private final IConnection iConnection;
	private final IStorage iStorage;

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
			this.iStorage.setVoteCount(0);
		}
	}

}
