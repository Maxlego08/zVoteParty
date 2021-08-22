package fr.maxlego08.zvoteparty.api.enums;

import fr.maxlego08.zvoteparty.exceptions.ButtonTypeException;

public enum ButtonType {

	NONE,

	NONE_SLOT,

	BACK,

	HOME,

	INVENTORY,

	PERFORM_COMMAND,
	
	MESSAGE,
	
	;

	/**
	 * 
	 * @param string
	 * @param inventoryName
	 * @return
	 * @throws ButtonTypeException
	 */
	public static ButtonType from(String string, String inventoryName, String real) throws ButtonTypeException {
		if (string != null)
			for (ButtonType type : values())
				if (type.name().equalsIgnoreCase(string))
					return type;
		throw new ButtonTypeException("Impossible de trouver le " + string + " pour type dans l'inventaire "
				+ inventoryName + " (" + real + ")");
	}

	public boolean isClickable() {
		return this != NONE;
	}

	public boolean isSlots() {
		return this == NONE_SLOT;
	}

	
	public boolean isNeedItems() {
		switch (this) {
		case BACK:
		case HOME:
		case INVENTORY:
		case NONE:
		case NONE_SLOT:
		case PERFORM_COMMAND:
		default:
			return false;
		}
	}

	public boolean isPageChange() {
		switch (this) {
		case BACK:
		case HOME:
		case INVENTORY:
		case NONE:
		case NONE_SLOT:
		case PERFORM_COMMAND:
		default:
			return false;
		}
	}

	public boolean isPermament() {
		switch (this) {
		case BACK:
			return false;
		case HOME:
		case INVENTORY:
		case NONE:
		case NONE_SLOT:
		case PERFORM_COMMAND:
		default:
			return true;
		}
	}

}
