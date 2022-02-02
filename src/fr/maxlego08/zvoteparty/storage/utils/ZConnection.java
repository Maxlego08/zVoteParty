package fr.maxlego08.zvoteparty.storage.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.storage.IConnection;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.storage.requets.InsertRunnable;
import fr.maxlego08.zvoteparty.storage.requets.SelectVoteCountRunnable;
import fr.maxlego08.zvoteparty.storage.requets.SelectVotesRunnable;
import fr.maxlego08.zvoteparty.storage.requets.UpdateCountRunnable;

public class ZConnection implements IConnection {

	private Connection connection;
	private final Storage storage;
	private final String user;
	private final String password;
	private final String host;
	private final String dataBase;
	private final int port;

	/**
	 * @param connection
	 * @param storage
	 * @param user
	 * @param password
	 * @param host
	 * @param dataBase
	 */
	public ZConnection(Storage storage, String user, String password, String host, String dataBase, int port) {
		super();
		this.storage = storage;
		this.user = user;
		this.password = password;
		this.host = host;
		this.dataBase = dataBase;
		this.port = port;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void asyncConnect() {
		Thread thread = new Thread("sql-connect") {

			@Override
			public void run() {
				try {
					connect();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		};
		thread.start();

	}

	@Override
	public void connect() throws SQLException {

		if (this.connection != null)
			return;

		String url = storage.getUrlBase() + host + ":" + port + "/" + dataBase;
		this.connection = DriverManager.getConnection(url, user, password);
	}

	@Override
	public void disconnect() {
		if (this.connection != null)
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void getAndRefreshConnection(Runnable runnable) {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							connect();
							runnable.run();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			} else {
				runnable.run();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateVoteCount(long amount) {
		this.getAndRefreshConnection(() -> {
			Thread thread = new Thread(new UpdateCountRunnable(this, amount));
			thread.start();
		});
	}

	@Override
	public void asyncFetchPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer) {
		this.getAndRefreshConnection(() -> {
			Thread thread = new Thread(new SelectVotesRunnable(this, offlinePlayer.getUniqueId(), consumer));
			thread.start();
		});
	}

	@Override
	public void asyncInsert(PlayerVote playerVote, Vote vote, Reward reward) {
		this.getAndRefreshConnection(() -> {
			Thread thread = new Thread(new InsertRunnable(playerVote, vote, reward, this));
			thread.start();
		});
	}

	@Override
	public void fetchVotes(IStorage sqlStorage) {
		this.getAndRefreshConnection(() -> {
			Runnable runnable = new SelectVoteCountRunnable(this, sqlStorage);
			runnable.run();
		});
	}

}
