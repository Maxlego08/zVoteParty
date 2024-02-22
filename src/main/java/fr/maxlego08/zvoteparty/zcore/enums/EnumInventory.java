package fr.maxlego08.zvoteparty.zcore.enums;

public enum EnumInventory {

	INVENTORY_DEFAULT(1),
	INVENTORY_CONFIG(2),
	
	;
	
	private final int id;

	private EnumInventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
