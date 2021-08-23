package fr.maxlego08.zvoteparty.api.enums;

public enum Permission {
	
	
	ZVOTEPARTY_USE,
	ZVOTEPARTY_RELOAD,
	ZVOTEPARTY_CONFIG,
	ZVOTEPARTY_ADD,
	ZVOTEPARTY_HELP,
	ZVOTEPARTY_VOTE,

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
