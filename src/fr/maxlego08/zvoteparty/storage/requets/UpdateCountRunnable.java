package fr.maxlego08.zvoteparty.storage.requets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.save.Config;

public class UpdateCountRunnable implements Runnable {

	private final IConnection connection;
	private final long value;

	/**
	 * @param connection
	 * @param value
	 */
	public UpdateCountRunnable(IConnection connection, long value) {
		super();
		this.connection = connection;
		this.value = value;
	}

	@Override
	public void run() {
		try {
			Connection connection = this.connection.getConnection();

			if (connection == null || connection.isClosed()) {
				this.connection.connect();
				connection = this.connection.getConnection();
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
			if (Config.enableDebug){			
				e.printStackTrace();
			}
		}
	}

}
