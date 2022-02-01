package fr.maxlego08.zvoteparty.api.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnection {

	/**
	 * 
	 * @return
	 */
	public Connection getConnection();

	/*
	 * 
	 */
	public void asyncConnect();

	/**
	 * 
	 * @throws SQLException
	 */
	public void connect() throws SQLException;

	/**
	 * 
	 */
	public void disconnect();

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public void getAndRefreshConnection(Runnable runnable);

}
