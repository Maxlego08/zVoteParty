package fr.maxlego08.zvoteparty.zcore.enums;

public enum Permission {
	
	
	ZVOTEPARTY_USE,
	ZVOTEPARTY_RELOAD,
	ZVOTEPARTY_HELP,

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
