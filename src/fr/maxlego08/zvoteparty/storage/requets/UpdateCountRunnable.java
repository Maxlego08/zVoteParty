package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;

public class UpdateCountRunnable implements Runnable {

	private final IConnection iConnection;
	private final long value;
	private int tryAmount = 0;

	/**
	 * @param connection
	 * @param value
	 */
	public UpdateCountRunnable(IConnection connection, long value) {
		super();
		this.iConnection = connection;
		this.value = value;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.iConnection.getConnection();

			if (connection == null || connection.isClosed()) {
				this.iConnection.connect();
				connection = this.iConnection.getConnection();
			}
			
			String selectRequest = "select count(*) as somme from zvoteparty_count";
			PreparedStatement statementSelect = connection.prepareStatement(selectRequest);
			ResultSet resultSetSelect = statementSelect.executeQuery();
			connection.commit();
			
			resultSetSelect.next();
			int value = resultSetSelect.getInt("somme");
			
			statementSelect.close();
			
			if (value < 1){
				
				String insertRequest = "insert into zvoteparty_count (vote) values (0);";
				PreparedStatement statement = connection.prepareStatement(insertRequest);
				statement.executeUpdate();
				connection.commit();
				statement.close();
				
			}
			
			String request = "UPDATE zvoteparty_count SET vote = ? where true";
			PreparedStatement statement = connection.prepareStatement(request);

			statement.setLong(1, this.value);
			
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
