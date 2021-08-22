package fr.maxlego08.zvoteparty.api.button.buttons;

import fr.maxlego08.zvoteparty.api.inventory.Inventory;

public interface InventoryButton extends PermissibleButton{

	/**
	 * 
	 * @return {@link Inventory}
	 */
	public Inventory getInventory();
	
}
