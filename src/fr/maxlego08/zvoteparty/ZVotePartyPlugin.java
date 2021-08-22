package fr.maxlego08.zvoteparty;

import org.bukkit.plugin.ServicePriority;

import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.command.CommandManager;
import fr.maxlego08.zvoteparty.command.commands.CommandIndex;
import fr.maxlego08.zvoteparty.inventory.InventoryManager;
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

	private final VotePartyManager manager = new ZVotePartyManager();

	@Override
	public void onEnable() {

		this.preEnable();

		this.commandManager = new CommandManager(this);
		this.inventoryManager = new InventoryManager(this);

		this.getServer().getServicesManager().register(VotePartyManager.class, this.manager, this,
				ServicePriority.High);

		/* Commands */
		
		this.registerCommand("zvoteparty", new CommandIndex(this), "voteparty", "vpF");
		
		
		/* Add Listener */

		this.addListener(new AdapterListener(this));
		this.addListener(inventoryManager);

		/* Add Saver */
		this.addSave(Config.getInstance());
		this.addSave(new MessageLoader(this));

		this.getSavers().forEach(saver -> saver.load(this.getPersist()));

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
	

}
