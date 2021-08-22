package fr.maxlego08.zvoteparty.button.buttons;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.button.Button;
import fr.maxlego08.zvoteparty.api.button.buttons.InventoryButton;
import fr.maxlego08.zvoteparty.api.enums.ButtonType;
import fr.maxlego08.zvoteparty.api.enums.PlaceholderAction;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.api.sound.SoundOption;

public class ZInventoryButton extends ZPlaceholderButton implements InventoryButton {

	protected String inventory;
	protected Inventory inventoryInterface;
	private final ZVotePartyPlugin plugin;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param isPermanent
	 * @param action
	 * @param placeholder
	 * @param value
	 * @param inventory
	 * @param inventoryInterface
	 * @param plugin
	 */
	public ZInventoryButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			String inventory, Inventory inventoryInterface, ZVotePartyPlugin plugin, boolean glow, SoundOption sound) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, glow,
				sound);
		this.inventory = inventory;
		this.inventoryInterface = inventoryInterface;
		this.plugin = plugin;
	}

	@Override
	public Inventory getInventory() {
		if (getType() == ButtonType.BACK)
			return this.inventoryInterface;
		return this.inventoryInterface == null ? this.inventoryInterface = this.plugin.getInventoryManager().getInventory(inventory, true)
				: this.inventoryInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZInventoryButton [inventory=" + inventory + ", inventoryInterface=" + inventoryInterface + "]";
	}

}
