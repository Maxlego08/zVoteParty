package fr.maxlego08.zvoteparty.zcore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.maxlego08.zvoteparty.adapter.PlayerAdapter;
import fr.maxlego08.zvoteparty.adapter.RewardAdapter;
import fr.maxlego08.zvoteparty.adapter.VoteAdapter;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.storage.Script;
import fr.maxlego08.zvoteparty.command.CommandManager;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.exceptions.ListenerNullException;
import fr.maxlego08.zvoteparty.inventory.VInventory;
import fr.maxlego08.zvoteparty.inventory.ZInventoryManager;
import fr.maxlego08.zvoteparty.listener.ListenerAdapter;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.enums.Folder;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.gson.LocationAdapter;
import fr.maxlego08.zvoteparty.zcore.utils.gson.PotionEffectAdapter;
import fr.maxlego08.zvoteparty.zcore.utils.nms.NMSUtils;
import fr.maxlego08.zvoteparty.zcore.utils.plugins.Plugins;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Saveable;

public abstract class ZPlugin extends JavaPlugin {

	private final Logger log = new Logger(this.getDescription().getFullName());
	private final List<Saveable> savers = new ArrayList<>();
	private final List<ListenerAdapter> listenerAdapters = new ArrayList<>();

	private Gson gson;
	private Persist persist;
	private long enableTime;

	protected CommandManager commandManager;
	protected ZInventoryManager zInventoryManager;

	private List<String> files = new ArrayList<>();

	protected void preEnable() {

		this.enableTime = System.currentTimeMillis();

		this.log.log("=== ENABLE START ===");
		this.log.log("Plugin Version V<&>c" + getDescription().getVersion(), LogType.INFO);

		this.getDataFolder().mkdirs();

		for (Folder folder : Folder.values()) {
			File currentFolder = new File(this.getDataFolder(), folder.toFolder());
			if (!currentFolder.exists())
				currentFolder.mkdir();
		}

		this.gson = getGsonBuilder().create();
		this.persist = new Persist(this);

		boolean isNew = NmsVersion.nmsVersion.isNewMaterial();
		for (String file : files) {
			if (isNew) {
				if (!new File(getDataFolder() + "/inventories/" + file + ".yml").exists())
					saveResource("inventories/1_13/" + file + ".yml", "inventories/" + file + ".yml", false);
			} else {
				if (!new File(getDataFolder() + "/inventories/" + file + ".yml").exists())
					saveResource("inventories/" + file + ".yml", false);
			}
		}

		for (Script script : Script.values()) {
			if (!new File(getDataFolder() + "/scripts/" + script.name().toLowerCase() + ".sql").exists()) {
				this.saveResource("scripts/" + script.name().toLowerCase() + ".sql", false);
			}
		}
	}

	protected void postEnable() {

		if (this.zInventoryManager != null)
			this.zInventoryManager.sendLog();

		if (this.commandManager != null)
			this.commandManager.validCommands();

		this.log.log(
				"=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	protected void preDisable() {
		this.enableTime = System.currentTimeMillis();
		this.log.log("=== DISABLE START ===");
	}

	protected void postDisable() {
		this.log.log(
				"=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	/**
	 * Build gson
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
				.registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter(this))
				.registerTypeAdapter(PlayerVote.class, new PlayerAdapter(this))
				.registerTypeAdapter(Vote.class, new VoteAdapter(this))
				.registerTypeAdapter(Reward.class, new RewardAdapter(this))
				.registerTypeAdapter(Location.class, new LocationAdapter(this));
	}

	/**
	 * Add a listener
	 * 
	 * @param listener
	 */
	public void addListener(Listener listener) {
		if (listener instanceof Saveable)
			this.addSave((Saveable) listener);
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	/**
	 * Add a listener from ListenerAdapter
	 * 
	 * @param adapter
	 */
	public void addListener(ListenerAdapter adapter) {
		if (adapter == null)
			throw new ListenerNullException("Warning, your listener is null");
		if (adapter instanceof Saveable)
			this.addSave((Saveable) adapter);
		this.listenerAdapters.add(adapter);
	}

	/**
	 * Add a Saveable
	 * 
	 * @param saver
	 */
	public void addSave(Saveable saver) {
		this.savers.add(saver);
	}

	/**
	 * Get logger
	 * 
	 * @return loggers
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * Get gson
	 * 
	 * @return {@link Gson}
	 */
	public Gson getGson() {
		return gson;
	}

	public Persist getPersist() {
		return persist;
	}

	/**
	 * Get all saveables
	 * 
	 * @return savers
	 */
	public List<Saveable> getSavers() {
		return savers;
	}

	/**
	 * 
	 * @param classz
	 * @return
	 */
	public <T> T getProvider(Class<T> classz) {
		RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
		if (provider == null) {
			log.log("Unable to retrieve the provider " + classz.toString(), LogType.WARNING);
			return null;
		}
		return provider.getProvider() != null ? (T) provider.getProvider() : null;
	}

	/**
	 * 
	 * @return listenerAdapters
	 */
	public List<ListenerAdapter> getListenerAdapters() {
		return listenerAdapters;
	}

	/**
	 * @return the commandManager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * @return the inventoryManager
	 */
	public ZInventoryManager getZInventoryManager() {
		return zInventoryManager;
	}

	/**
	 * Check if plugin is enable
	 * 
	 * @param pluginName
	 * @return
	 */
	protected boolean isEnable(Plugins pl) {
		Plugin plugin = getPlugin(pl);
		return plugin == null ? false : plugin.isEnabled();
	}

	/**
	 * Get plugin for plugins enum
	 * 
	 * @param pluginName
	 * @return
	 */
	protected Plugin getPlugin(Plugins plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin.getName());
	}

	/**
	 * Register command
	 * 
	 * @param command
	 * @param vCommand
	 * @param aliases
	 */
	protected void registerCommand(String command, VCommand vCommand, String... aliases) {
		this.commandManager.registerCommand(command, vCommand, aliases);
	}

	/**
	 * Register Inventory
	 * 
	 * @param inventory
	 * @param vInventory
	 */
	protected void registerInventory(EnumInventory inventory, VInventory vInventory) {
		this.zInventoryManager.registerInventory(inventory, vInventory);
	}

	public List<String> getFiles() {
		return files;
	}

	/**
	 * 
	 * @param resourcePath
	 * @param toPath
	 * @param replace
	 */
	protected void saveResource(String resourcePath, String toPath, boolean replace) {
		if (resourcePath != null && !resourcePath.equals("")) {
			resourcePath = resourcePath.replace('\\', '/');
			InputStream in = this.getResource(resourcePath);
			if (in == null) {
				throw new IllegalArgumentException(
						"The embedded resource '" + resourcePath + "' cannot be found in " + this.getFile());
			} else {
				File outFile = new File(getDataFolder(), toPath);
				int lastIndex = toPath.lastIndexOf(47);
				File outDir = new File(getDataFolder(), toPath.substring(0, lastIndex >= 0 ? lastIndex : 0));
				if (!outDir.exists()) {
					outDir.mkdirs();
				}

				try {
					if (outFile.exists() && !replace) {
						getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile
								+ " because " + outFile.getName() + " already exists.");
					} else {
						OutputStream out = new FileOutputStream(outFile);
						byte[] buf = new byte[1024];

						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						out.close();
						in.close();
					}
				} catch (IOException var10) {
					getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
				}

			}
		} else {
			throw new IllegalArgumentException("ResourcePath cannot be null or empty");
		}
	}

	protected void registerFile(InventoryName file) {
		this.files.add(file.getName());
	}
}
