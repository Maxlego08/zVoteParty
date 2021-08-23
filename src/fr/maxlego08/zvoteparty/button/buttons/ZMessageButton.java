package fr.maxlego08.zvoteparty.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zvoteparty.api.button.Button;
import fr.maxlego08.zvoteparty.api.button.buttons.MessageButton;
import fr.maxlego08.zvoteparty.api.enums.ButtonType;
import fr.maxlego08.zvoteparty.api.enums.PlaceholderAction;
import fr.maxlego08.zvoteparty.api.sound.SoundOption;

public class ZMessageButton extends ZPlaceholderButton implements MessageButton {

	private final List<String> messages;
	private boolean closeInventory;



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
	 * @param needGlow
	 * @param sound
	 * @param messages
	 * @param closeInventory
	 */
	public ZMessageButton(ButtonType type, ItemStack itemStack, int slot, String permission, String message,
			Button elseButton, boolean isPermanent, PlaceholderAction action, String placeholder, String value,
			boolean needGlow, SoundOption sound, List<String> messages, boolean closeInventory) {
		super(type, itemStack, slot, permission, message, elseButton, isPermanent, action, placeholder, value, needGlow,
				sound);
		this.messages = messages;
		this.closeInventory = closeInventory;
	}

	@Override
	public List<String> getMessages() {
		return this.messages;
	}

	@Override
	public boolean closeInventory() {
		return this.closeInventory;
	}

	@Override
	public void send(Player player) {
		this.messages.forEach(message -> messageWO(player, message, "%player%", player.getName()));
	}

}
