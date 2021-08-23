package fr.maxlego08.zvoteparty;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.command.Command;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.command.CommandObject;
import fr.maxlego08.zvoteparty.implementations.ZReward;
import fr.maxlego08.zvoteparty.inventory.ZInventoryManager;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZVotePartyManager extends ZUtils implements VotePartyManager {

	private final ZVotePartyPlugin plugin;

	/**
	 * @param plugin
	 */
	public ZVotePartyManager(ZVotePartyPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void reload(CommandSender sender) {

		try {

			this.plugin.reloadConfig();
			this.plugin.getInventoryManager().loadInventories();
			this.loadConfiguration();
			message(sender, Message.RELOAD_SUCCESS);

		} catch (Exception e) {

			e.printStackTrace();
			message(sender, Message.RELOAD_SUCCESS);

		}

	}

	@Override
	public void loadConfiguration() {
		// TODO Auto-generated method stub

	}

	@Override
	public void openVote(Player player) {
		if (Config.enableVoteMessage)
			message(player, Message.VOTE_INFORMATIONS);

		Inventory inventory = this.plugin.getInventoryManager().getInventory(InventoryName.VOTE);
		Command command = CommandObject.of(inventory);
		
		ZInventoryManager inventoryManager = this.plugin.getZInventoryManager();
		inventoryManager.createInventory(EnumInventory.INVENTORY_DEFAULT, player, 1, inventory, new ArrayList<>(), command);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void vote(String username, String serviceName) {
		
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
		
		if (offlinePlayer != null){
			
			this.vote(offlinePlayer, serviceName);
			
		} else
			Logger.info("Impossible to find the player " + username, LogType.WARNING);
		
		this.handleVoteParty();
	}

	@Override
	public void handleVoteParty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vote(CommandSender sender, OfflinePlayer player) {
		
	}

	@Override
	public void vote(OfflinePlayer offlinePlayer, String serviceName) {
		PlayerVote playerVote = this.plugin.get(offlinePlayer);
		Reward reward = this.getRandomReward();
		playerVote.vote(serviceName, reward);
	}

	public Reward getRandomReward() {
		return new ZReward(100, Arrays.asList("bc %player% vient de voter"), false);
	}

}
