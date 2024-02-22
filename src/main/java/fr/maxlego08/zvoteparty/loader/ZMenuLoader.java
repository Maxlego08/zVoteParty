package fr.maxlego08.zvoteparty.loader;

import java.io.File;
import java.util.Optional;

import org.bukkit.entity.Player;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZMenuLoader extends ZUtils {

	private InventoryManager inventoryManager;

	private final ZVotePartyPlugin plugin;

	/**
	 * @param plugin
	 */
	public ZMenuLoader(ZVotePartyPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public void load() {
		this.inventoryManager = plugin.getProvider(InventoryManager.class);
	}

	public void reload() {

		File file = new File(this.plugin.getDataFolder(), "inventories/" + InventoryName.VOTE.getName() + ".yml");
		try {
			this.inventoryManager.deleteInventories(this.plugin);
			this.inventoryManager.loadInventory(this.plugin, file);
		} catch (InventoryException e) {
			e.printStackTrace();
		}
	}

	public void open(Player player) {
		Optional<Inventory> optional = this.inventoryManager.getInventory(InventoryName.VOTE.getName());
		if (optional.isPresent()) {
			Inventory inventory = optional.get();
			this.inventoryManager.openInventory(player, inventory);
		} else
			message(player, "§cErreur with inventory votes !");
	}

}
