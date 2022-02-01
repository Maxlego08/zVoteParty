package fr.maxlego08.zvoteparty.api.storage;

public enum Storage {

	MYSQL("jdbc:mysql://"),

	MARIADB("jdbc:mariadb://"), 
	PGSQL("jdbc:postgresql://"), 
	SQLITE(""), 
	JSON,
	
	REDIS,
	
	CUSTOM,

	;

	private final String urlBase;

	/**
	 * @param urlBase
	 */
	private Storage(String urlBase) {
		this.urlBase = urlBase;
	}

	private Storage() {
		this(null);
	}

	public String getUrlBase() {
		return urlBase;
	}

	public boolean isDatabase(){
		switch (this) {
		case CUSTOM:
		case REDIS:
		case JSON:
			return false;
		case SQLITE:
		case MARIADB:
		case MYSQL:
		case PGSQL:
		default:
			return true;
		}
	}
	
	public boolean isDefault() {
		switch (this) {
		case CUSTOM:
		case REDIS:
			return false;
		case SQLITE:
		case JSON:
		case MARIADB:
		case MYSQL:
		case PGSQL:
		default:
			return true;
		}
	}

}
