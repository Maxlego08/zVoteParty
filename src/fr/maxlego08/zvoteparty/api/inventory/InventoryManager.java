package fr.maxlego08.zvoteparty.api.inventory;

import java.util.Collection;
import java.util.Optional;

import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.enums.InventoryType;

public interface InventoryManager {

	/**
	 * Get inventory by name
	 * 
	 * @param name
	 * @return inventory
	 */
	public Inventory getInventory(InventoryName name);
	
	/**
	 * 
	 * @param name
	 * @param error
	 * @return
	 */
	public Inventory getInventory(String name, boolean error);

	/**
	 * 
	 * @param type
	 * @return
	 */
	public Inventory getInventory(InventoryType type);

	/**
	 * Load inventories
	 */
	public void loadInventories() throws Exception;

	/**
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public Inventory loadInventory(String fileName) throws Exception;

	/**
	 * Save inventories
	 */
	public void saveInventories();

	/**
	 * Delete all inventory
	 */
	public void delete();

	/**
	 * Get inventory by name
	 * 
	 * @param name
	 * @return {@link Inventory}
	 */
	public Optional<Inventory> getInventoryByName(String name);
	
	/**
	 * 
	 * @return
	 */
	public Collection<Inventory> getInventories();

}
