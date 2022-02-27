package fr.maxlego08.zvoteparty;

import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.inventory.InventoryManager;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.StorageManager;
import fr.maxlego08.zvoteparty.command.CommandManager;
import fr.maxlego08.zvoteparty.command.commands.CommandIndex;
import fr.maxlego08.zvoteparty.command.commands.CommandVote;
import fr.maxlego08.zvoteparty.inventory.InventoryLoader;
import fr.maxlego08.zvoteparty.inventory.ZInventoryManager;
import fr.maxlego08.zvoteparty.inventory.inventories.InventoryConfig;
import fr.maxlego08.zvoteparty.inventory.inventories.InventoryDefault;
import fr.maxlego08.zvoteparty.listener.AdapterListener;
import fr.maxlego08.zvoteparty.listener.listeners.VoteListener;
import fr.maxlego08.zvoteparty.listener.listeners.VotifierListener;
import fr.maxlego08.zvoteparty.placeholder.VotePartyExpansion;
import fr.maxlego08.zvoteparty.placeholder.ZPlaceholderApi;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.save.MessageLoader;
import fr.maxlego08.zvoteparty.storage.ZStorageManager;
import fr.maxlego08.zvoteparty.zcore.ZPlugin;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.utils.plugins.Metrics;
import fr.maxlego08.zvoteparty.zcore.utils.plugins.Plugins;
import fr.maxlego08.zvoteparty.zcore.utils.plugins.VersionChecker;

/**
 * System to create your plugins very simply Projet:
 * https://github.com/Maxlego08/TemplatePlugin
 * 
 * @author Maxlego08
 *
 */
public class ZVotePartyPlugin extends ZPlugin {

	private final VotePartyManager manager = new ZVotePartyManager(this);
	private final InventoryManager inventoryManager = new InventoryLoader(this);

	private StorageManager storageManager;

	@Override
	public void onEnable() {

		ZPlaceholderApi.getInstance().setPlugin(this);

		/* Register inventories */

		for (InventoryName inventoryName : InventoryName.values())
			this.registerFile(inventoryName);

		this.preEnable();

		this.saveDefaultConfig();

		this.commandManager = new CommandManager(this);
		this.zInventoryManager = new ZInventoryManager(this);

		this.getServer().getServicesManager().register(VotePartyManager.class, this.manager, this,
				ServicePriority.High);

		/* Commands */

		this.registerCommand("zvoteparty", new CommandIndex(this), "voteparty", "vp");

		/* Inventories */

		this.registerInventory(EnumInventory.INVENTORY_DEFAULT, new InventoryDefault());
		this.registerInventory(EnumInventory.INVENTORY_CONFIG, new InventoryConfig());

		/* Add Listener */

		this.addListener(new AdapterListener(this));
		this.addListener(this.zInventoryManager);
		this.addListener(new VoteListener(this));

		/* Add Saver */
		this.addSave(Config.getInstance());
		this.addSave(new MessageLoader(this));
		this.addSave(this.manager);

		this.getSavers().forEach(saver -> saver.load(this.getPersist()));

		// Load storage

		this.storageManager = new ZStorageManager(Config.storage, this);
		this.storageManager.load(this.getPersist());

		try {
			this.inventoryManager.loadInventories();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Config.enableVoteCommand) {
			this.registerCommand("vote", new CommandVote(this));
		}

		this.manager.loadConfiguration();

		if (Config.enableAutoUpdate) {
			// Timer timer = new Timer();
			// timer.schedule(new UpdateTimer(), Config.autoSaveSecond);
		}

		if (this.isEnable(Plugins.PLACEHOLDER)) {
			VotePartyExpansion expansion = new VotePartyExpansion(this);
			expansion.register();
		}

		if (this.isEnable(Plugins.VOTIFIER)){
			this.getLog().log("Hook NuVotifier");
			this.addListener(new VotifierListener(this));
		}
		
		VersionChecker checker = new VersionChecker(this, 124);
		checker.useLastVersion();

		new Metrics(this, 12543);

		this.postEnable();
	}

	@Override
	public void onDisable() {
		this.preDisable();

		this.getSavers().forEach(saver -> saver.save(this.getPersist()));
		this.storageManager.save(this.getPersist());

		this.postDisable();
	}

	/**
	 * Return the manager for the voteparty
	 * 
	 * @return {@link VotePartyManager}
	 */
	public VotePartyManager getManager() {
		return manager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public PlayerManager getPlayerManager() {
		return this.storageManager.getIStorage();
	}

	public IStorage getIStorage() {
		return this.storageManager.getIStorage();
	}

	/**
	 * Get player vote
	 * 
	 * @param offlinePlayer
	 * @return {@link PlayerVote}
	 */
	public void get(OfflinePlayer offlinePlayer, Consumer<PlayerVote> consumer, boolean forceDatabaseUpdate) {
		this.get(offlinePlayer.getUniqueId(), consumer, forceDatabaseUpdate);
	}
	
	/**
	 * Get player vote
	 * 
	 * @param offlinePlayer
	 * @return {@link PlayerVote}
	 */
	public void get(UUID uuid, Consumer<PlayerVote> consumer, boolean forceDatabaseUpdate) {
		PlayerManager manager = this.getPlayerManager();
		manager.getPlayer(uuid, optional -> {
			consumer.accept(optional.isPresent() ? optional.get() : manager.createPlayer(uuid));
		}, true);
	}

}
