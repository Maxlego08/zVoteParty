package fr.maxlego08.zvoteparty.save;

public class RedisConfiguration {
	private String host;
	private int port;
	private String password;
	private int databaseIndex;

	/**
	 * @param host
	 * @param port
	 * @param password
	 * @param databaseIndex
	 * @param poolConfig
	 */
	public RedisConfiguration(String host, int port, String password, int databaseIndex,
			RedisPoolConfiguration poolConfig) {
		super();
		this.host = host;
		this.port = port;
		this.password = password;
		this.databaseIndex = databaseIndex;
		this.poolConfig = poolConfig;
	}

	private RedisPoolConfiguration poolConfig;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getPassword() {
		return password;
	}

	public RedisPoolConfiguration getPoolConfig() {
		return poolConfig;
	}

	public int getDatabaseIndex() {
		return databaseIndex;
	}

	public static class RedisPoolConfiguration {
		private int maxTotal;
		private int maxIdle;
		private int minIdle;

		/**
		 * @param maxTotal
		 * @param maxIdle
		 * @param minIdle
		 */
		public RedisPoolConfiguration(int maxTotal, int maxIdle, int minIdle) {
			super();
			this.maxTotal = maxTotal;
			this.maxIdle = maxIdle;
			this.minIdle = minIdle;
		}

		public int getMaxTotal() {
			return maxTotal;
		}

		public int getMaxIdle() {
			return maxIdle;
		}

		public int getMinIdle() {
			return minIdle;
		}
	}
}
