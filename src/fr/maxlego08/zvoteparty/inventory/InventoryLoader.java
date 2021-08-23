package fr.maxlego08.zvoteparty.inventory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.button.Button;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.enums.InventoryType;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.api.inventory.InventoryManager;
import fr.maxlego08.zvoteparty.exceptions.InventoryFileNotFoundException;
import fr.maxlego08.zvoteparty.exceptions.InventoryNotFoundException;
import fr.maxlego08.zvoteparty.exceptions.InventorySizeException;
import fr.maxlego08.zvoteparty.exceptions.NameAlreadyExistException;
import fr.maxlego08.zvoteparty.loader.ButtonCollections;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.loader.Loader;
import fr.maxlego08.zvoteparty.zcore.utils.yaml.YamlUtils;

public class InventoryLoader extends YamlUtils implements InventoryManager {

	private final ZVotePartyPlugin plugin;
	private final Map<InventoryType, Inventory> typeInventories = new HashMap<>();
	private final Map<String, Inventory> inventories = new HashMap<String, Inventory>();

	/**
	 * @param plugin
	 * @param plugin2
	 * @param economy
	 */
	public InventoryLoader(ZVotePartyPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public Inventory getInventory(InventoryName name) {
		return this.getInventory(name.getName(), true);
	}

	/**
	 * 
	 * @param name
	 * @param throwError
	 * @return
	 */
	public Inventory getInventory(String name, boolean throwError) {
		if (name == null && throwError)
			throw new InventoryNotFoundException("Unable to find the inventory with name null");
		Inventory inventory = inventories.getOrDefault(name.toLowerCase(), null);
		if (inventory == null && throwError)
			throw new InventoryNotFoundException("Unable to find the inventory " + name);
		return inventory;
	}

	@Override
	public void loadInventories() throws Exception {

		info("Loading inventories in progress...");

		FileConfiguration config = super.getConfig();

		this.delete();

		this.plugin.getFiles().forEach(file -> {
			try {
				this.loadInventory(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		if (config.contains("customInventories")) {
			List<String> stringCategories = config.getStringList("customInventories");
			for (String stringCategory : stringCategories)
				this.loadInventory(stringCategory);
		}

		info("Inventories loading complete.");
		
		if (Config.enableInventoryPreRender) {
			this.inventories.values().forEach(Inventory::renderPermanentButtons);
			info("Inventories render permanent buttons.");
		}

	}

	@Override
	public void saveInventories() {

	}

	@Override
	public Inventory loadInventory(String fileName) throws Exception {

		if (fileName == null)
			throw new NullPointerException("Impossible de trouver le string ! Il est null !");

		String lowerCategory = fileName.toLowerCase();

		if (inventories.containsKey(lowerCategory))
			throw new NameAlreadyExistException("the name " + lowerCategory
					+ " already exist ! (Simply remove it from the list of categories in the config.yml file.)");

		YamlConfiguration configuration = getConfig("inventories/" + lowerCategory + ".yml");

		if (configuration == null){
			Logger.info("Please check that your file is in lower case!", LogType.ERROR);
			throw new InventoryFileNotFoundException("Cannot find the file: inventories/" + lowerCategory
					+ ".yml . Do you have the files in the zAuctionHouseV3/inventories/ folder?");
		}

		InventoryType type = InventoryType.form(configuration.getString("type"));

		String name = configuration.getString("name");
		name = name == null ? "" : name;

		int size = configuration.getInt("size", 54);
		if (size % 9 != 0)
			throw new InventorySizeException("Size " + size + " is not valid for inventory " + lowerCategory);

		Loader<List<Button>> loader = new ButtonCollections(plugin);
		List<Button> buttons = loader.load(configuration, lowerCategory);

		Inventory inventory = new InventoryObject(name, type, size, buttons, lowerCategory);
		inventories.put(lowerCategory, inventory);

		if (type != InventoryType.DEFAULT)
			typeInventories.put(type, inventory);

		success("Successful loading of the inventory " + lowerCategory + " !");
		return inventory;
	}

	@Override
	public void delete() {
		inventories.clear();
		typeInventories.clear();
	}

	@Override
	public Inventory getInventory(InventoryType type) {
		return typeInventories.getOrDefault(type, null);
	}

	@Override
	public Optional<Inventory> getInventoryByName(String name) {
		Inventory inventory = getInventory(name, false);
		if (inventory != null)
			return Optional.of(inventory);
		return inventories.values().stream().filter(inv -> inv.getName().toLowerCase().contains(name.toLowerCase()))
				.findFirst();
	}

	@Override
	public Collection<Inventory> getInventories() {
		return Collections.unmodifiableCollection(this.inventories.values());
	}

}
