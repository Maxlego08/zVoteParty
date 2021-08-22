package fr.maxlego08.zvoteparty.api.button.buttons;

import java.util.List;

import org.bukkit.entity.Player;

public interface MessageButton extends PlaceholderButton {

	/**
	 * Allows you to know if the inventory should be closed
	 * 
	 * @return boolean
	 */
	boolean closeInventory();
	
	/**
	 * Return the list of messages
	 * 
	 * @return messages
	 */
	List<String> getMessages();

	/**
	 * Sends messages to a player
	 * 
	 * @param player
	 */
	void send(Player player);
	
}
