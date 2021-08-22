package fr.maxlego08.zvoteparty;

import org.bukkit.plugin.ServicePriority;

import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.inventory.InventoryManager;
import fr.maxlego08.zvoteparty.command.CommandManager;
import fr.maxlego08.zvoteparty.command.commands.CommandIndex;
import fr.maxlego08.zvoteparty.command.commands.CommandVote;
import fr.maxlego08.zvoteparty.inventory.InventoryLoader;
import fr.maxlego08.zvoteparty.inventory.ZInventoryManager;
import fr.maxlego08.zvoteparty.listener.AdapterListener;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.save.MessageLoader;
import fr.maxlego08.zvoteparty.zcore.ZPlugin;

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

	@Override
	public void onEnable() {

		this.preEnable();
		
		/* Register inventories */

		for (InventoryName inventoryName : InventoryName.values())
			this.registerFile(inventoryName);
		
		this.saveDefaultConfig();

		this.commandManager = new CommandManager(this);
		this.zInventoryManager = new ZInventoryManager(this);

		this.getServer().getServicesManager().register(VotePartyManager.class, this.manager, this,
				ServicePriority.High);
	

		/* Commands */

		this.registerCommand("zvoteparty", new CommandIndex(this), "voteparty", "vp");

		/* Add Listener */

		this.addListener(new AdapterListener(this));
		this.addListener(this.zInventoryManager);
		this.addListener((ZVotePartyManager) this.manager);

		/* Add Saver */
		this.addSave(Config.getInstance());
		this.addSave(new MessageLoader(this));

		this.getSavers().forEach(saver -> saver.load(this.getPersist()));

		try {
			this.inventoryManager.loadInventories();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (Config.enableVoteCommand){
			this.registerCommand("vote", new CommandVote(this));
		}
		
		this.manager.loadConfiguration();
		
		this.postEnable();
	}

	@Override
	public void onDisable() {
		this.preDisable();

		this.getSavers().forEach(saver -> saver.save(this.getPersist()));

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
	
}
