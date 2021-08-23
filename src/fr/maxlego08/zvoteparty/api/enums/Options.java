package fr.maxlego08.zvoteparty.api.enums;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.save.Config;

public enum Options {

	ENABLE_DEBUG("enableDebug", "Enables the debug mode of the plugin.", "You will thus be able to obtain information", "in your console."),
	
	ENABLE_DEBUG_TIME("enableDebugTime", "Enable debug time of the plugin", "This is a debugging tool."),
	
	ENABLE_LOG_MESSAGE("enableLogMessage", "Enable log message in console", "This is a debugging tool."),
	
	ENABLE_VERSION_CHECKER("enableAutoUpdate", "Enable plugin version checker."),
	
	ENABLE_INVENTORY_PRE_RENDER("enableInventoryPreRender", "Allows you to make items that are permanent."),
	
	ENABLE_OPEN_SYNC_INVENTORY("enableOpenSyncInventory", "Allows to open the inventory with the items in a synchronized way."),
	
	ENABLE_VOTE_COMMAND("enableVoteCommand", "Allows you to activate the /vote command."),
	
	ENABLE_VOTE_INVENTORY("enableVoteInventory", "Allows you to open the inventory with the /vote command."),
	
	ENABLE_VOTE_MESSAGE("enableVoteMessage", "Allows you to display the message in the /vote command."),
	
	ENABLE_ACTION_BAR_BROADCAST("enableActionBarVoteAnnonce", "Allows you to activate the bar action when a player votes."),
	
	ENABLE_TCHAT_BROADCAST("enableTchatVoteAnnonce", "Allows you to activate the broadcast message when a player votes."),

	;

	private final String fieldName;
	private final List<String> descriptions;
	private final Material material;

	/**
	 * @param name
	 * @param material
	 * @param descriptions
	 */
	private Options(String name, String... strings) {
		this.fieldName = name;
		this.material = Material.PAPER;
		this.descriptions = Arrays.asList(strings);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return fieldName;
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @return the descriptions
	 */
	public List<String> getDescriptions() {
		return descriptions;
	}

	public boolean toggle(ZVotePartyPlugin plugin) {

		try {
			Class<Config> classz = Config.class;
			Field field = classz.getDeclaredField(fieldName);
			field.set(classz, !(Boolean) field.get(classz));
			
			
			if (this.isToggle() && this == ENABLE_INVENTORY_PRE_RENDER){
				plugin.getInventoryManager().getInventories().forEach(Inventory::renderPermanentButtons);
			}
			
			Config.getInstance().save(plugin.getPersist());
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean isToggle() {

		try {
			Class<Config> classz = Config.class;
			Field field = classz.getDeclaredField(fieldName);
			return (Boolean) field.get(classz);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
