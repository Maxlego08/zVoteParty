package fr.maxlego08.zvoteparty.api.button.buttons;

import fr.maxlego08.zvoteparty.api.inventory.Inventory;

public interface BackButton extends InventoryButton {

	/**
	 * Set back inventory
	 * @param inventory
	 */
	public void setBackInventory(Inventory inventory);
	
}
