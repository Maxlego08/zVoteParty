package fr.maxlego08.zvoteparty.api.enums;

public enum InventoryType {

	DEFAULT,
	VOTE,

	;

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static InventoryType form(String string) {
		if (string == null)
			return DEFAULT;
		try {
			InventoryType type = valueOf(string.toUpperCase());
			return type == null ? DEFAULT : type;
		} catch (Exception e) {
			return DEFAULT;
		}
	}

	public boolean isDefault() {
		return true;
	}

}
