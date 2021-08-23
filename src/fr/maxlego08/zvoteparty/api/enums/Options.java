package fr.maxlego08.zvoteparty.api.enums;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.save.Config;

public enum Options {

	ENABLE_DEBUG_MODE("enableDebugMode", "Enables the debug mode of the plugin.", "You will thus be able to obtain information", "in your console."),
	
	ENABLE_VERSION_CHECKER("enableVersionChecker", "Enable plugin version checker"),
	
	ENABLE_INVENTORY_PRE_RENDER("enableInventoryPreRender", "Allows you to make items that are permanent."),

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
