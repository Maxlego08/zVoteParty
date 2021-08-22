package fr.maxlego08.zvoteparty.api.enums;

public enum InventoryName {

	VOTE("vote"),
	
	;

	private final String name;

	/**
	 * @param name
	 */
	private InventoryName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
