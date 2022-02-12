package fr.maxlego08.zvoteparty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.command.Command;
import fr.maxlego08.zvoteparty.api.enums.InventoryName;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.RewardType;
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.command.CommandObject;
import fr.maxlego08.zvoteparty.inventory.ZInventoryManager;
import fr.maxlego08.zvoteparty.loader.RewardLoader;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.zcore.enums.EnumInventory;
import fr.maxlego08.zvoteparty.zcore.logger.Logger;
import fr.maxlego08.zvoteparty.zcore.logger.Logger.LogType;
import fr.maxlego08.zvoteparty.zcore.utils.loader.Loader;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;
import fr.maxlego08.zvoteparty.zcore.utils.yaml.YamlUtils;

public class ZVotePartyManager extends YamlUtils implements VotePartyManager {

	private final ZVotePartyPlugin plugin;
	private final List<Reward> rewards = new ArrayList<>();

	private final List<Reward> partyRewards = new ArrayList<>();
	private List<String> globalCommands = new ArrayList<>();
	private List<String> commands = new ArrayList<>();
	private long needVote = 50;

	/**
	 * @param plugin
	 */
	public ZVotePartyManager(ZVotePartyPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void reload(CommandSender sender) {

		try {

			this.plugin.reloadConfig();
			this.plugin.getInventoryManager().loadInventories();
			this.loadConfiguration();
			this.plugin.getSavers().forEach(e -> e.load(this.plugin.getPersist()));

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

		if (Config.enableVoteMessage) {

			message(player, Message.VOTE_INFORMATIONS);

		}

		if (Config.enableVoteInventory) {

			Inventory inventory = this.plugin.getInventoryManager().getInventory(InventoryName.VOTE);
			Command command = CommandObject.of(inventory);

			ZInventoryManager inventoryManager = this.plugin.getZInventoryManager();
			inventoryManager.createInventory(EnumInventory.INVENTORY_DEFAULT, player, 1, inventory, new ArrayList<>(),
					command);
			return;
		}

		if (!Config.enableVoteMessage) {
			message(player, "§cError in configuration, please contact an administrator.");
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void vote(String username, String serviceName, boolean updateVoteParty) {

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

		this.handleVoteParty();

		if (offlinePlayer != null) {

			this.vote(offlinePlayer, serviceName);

		} else {
			// If the player cannot be found we will call redis
			IStorage iStorage = this.plugin.getIStorage();
			iStorage.performCustomVoteAction(username, serviceName, null);
		}

	}

	@Override
	public void handleVoteParty() {

		IStorage iStorage = this.plugin.getIStorage();
		iStorage.addVoteCount(1);

		if (iStorage.getVoteCount() >= this.needVote) {
			this.start();
		}

	}

	@Override
	public void vote(CommandSender sender, String username, boolean updateVoteParty) {

		this.vote(username, "Serveur Minecraft Vote", updateVoteParty);
		message(sender, Message.VOTE_SEND, "%player%", username);

	}

	@Override
	public void vote(OfflinePlayer offlinePlayer, String serviceName) {

		Reward reward = this.getRandomReward(RewardType.VOTE);
		IStorage iStorage = this.plugin.getIStorage();

		// If the redis configuration is active, the reward is online and the
		// user is not connected then we will call redis
		if (reward.needToBeOnline() && Config.storage.equals(Storage.REDIS) && !offlinePlayer.isOnline()) {
			iStorage.performCustomVoteAction(offlinePlayer.getName(), serviceName, offlinePlayer.getUniqueId());
			return;
		}

		// We will retrieve the PlayerVote object in asymmetric in the database
		// and execute the vote
		this.plugin.get(offlinePlayer, playerVote -> {
			Vote vote = playerVote.vote(this.plugin, serviceName, reward, false);
			iStorage.insertVote(playerVote, vote, reward);
		}, false);

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean secretVote(String username, String serviceName) {

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

		// We will get the offline player and check that it is online
		if (offlinePlayer == null || !offlinePlayer.isOnline()) {
			return false;
		}

		// We'll get the reward
		Reward reward = this.getRandomReward(RewardType.VOTE);

		// If the reward is online
		if (reward.needToBeOnline()) {

			// We will retrieve the PlayerVote object in asymmetric in the
			// database and execute the vote
			this.plugin.get(offlinePlayer, playerVote -> {
				IStorage iStorage = this.plugin.getIStorage();
				Vote vote = playerVote.vote(this.plugin, serviceName, reward, false);
				iStorage.insertVote(playerVote, vote, reward);
			}, false);
			return true;
		}

		return false;
	}

	@Override
	public Reward getRandomReward(RewardType type) {

		double percent = ThreadLocalRandom.current().nextDouble(0, 100);
		Reward reward = randomElement(type == RewardType.VOTE ? this.rewards : this.partyRewards);
		if (reward.getPercent() <= percent || reward.getPercent() >= 100) {
			return reward;
		}

		return this.getRandomReward(type);

	}

	@Override
	public void giveVotes(Player player) {
		this.plugin.get(player, playerVote -> {
			List<Vote> votes = playerVote.getNeedRewardVotes();
			if (votes.size() > 0) {
				schedule(Config.joinGiveVoteMilliSecond, () -> {
					message(player, Message.VOTE_LATER, "%amount%", votes.size());
					votes.forEach(e -> e.giveReward(this.plugin, player));
				});
				IStorage iStorage = this.plugin.getIStorage();
				iStorage.updateRewards(player.getUniqueId());
			}
		}, true);

	}

	@Override
	public void save(Persist persist) {

	}

	@Override
	public void load(Persist persist) {

		YamlConfiguration configuration = this.getConfig();
		ConfigurationSection configurationSection = configuration.getConfigurationSection("rewards.");
		Loader<Reward> loader = new RewardLoader();

		this.rewards.clear();

		for (String key : configurationSection.getKeys(false)) {
			String path = "rewards." + key + ".";
			Reward reward = loader.load(configuration, path);
			this.rewards.add(reward);
		}

		Logger.info("Loaded " + this.rewards.size() + " rewards", LogType.SUCCESS);

		// Party loader

		this.needVote = configuration.getLong("party.votes_needed", 50);
		this.globalCommands = configuration.getStringList("party.global_commands");
		this.commands = configuration.getStringList("party.commands");

		configurationSection = configuration.getConfigurationSection("party.rewards.");
		this.partyRewards.clear();

		for (String key : configurationSection.getKeys(false)) {
			String path = "party.rewards." + key + ".";
			Reward reward = loader.load(configuration, path);
			this.partyRewards.add(reward);
		}
	}

	@Override
	public List<String> getGlobalCommands() {
		return this.globalCommands;
	}

	@Override
	public List<Reward> getPartyReward() {
		return this.partyRewards;
	}

	@Override
	public long getNeedVotes() {
		return this.needVote;
	}

	@Override
	public long getPlayerVoteCount(Player player) {
		PlayerManager manager = this.plugin.getPlayerManager();
		// We will retrieve the player in a symmetrical way, we will not search
		// in the database
		Optional<PlayerVote> optional = manager.getSyncPlayer(player);
		if (optional.isPresent()) {
			PlayerVote playerVote = optional.get();
			return playerVote.getVoteCount();
		}
		return 0;
	}

	@Override
	public void sendNeedVote(CommandSender sender) {
		message(sender, Message.VOTE_NEEDED);
	}

	@Override
	public void forceStart(CommandSender sender) {
		message(sender, Message.VOTE_STARTPARTY);
		this.start();
	}

	@Override
	public void start() {

		IStorage iStorage = this.plugin.getIStorage();
		iStorage.startVoteParty();
		this.secretStart();

	}

	@Override
	public void secretStart() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Bukkit.getScheduler().runTask(this.plugin, () -> {
				this.globalCommands.forEach(command -> {
					command = command.replace("%player%", player.getName());
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.papi(command, player));
				});
			});

			Reward reward = this.getRandomReward(RewardType.PARTY);
			reward.give(this.plugin, player);

		}

		Bukkit.getScheduler().runTask(this.plugin, () -> {
			this.commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
		});

		broadcast(Message.VOTE_PARTY_START);
	}

	@Override
	public void removeVote(CommandSender sender, OfflinePlayer player) {
		PlayerManager manager = this.plugin.getPlayerManager();
		manager.getPlayer(player, optional -> {

			if (!optional.isPresent()) {
				message(sender, Message.VOTE_REMOVE_ERROR, "%player%", player.getName());
				return;
			}
			PlayerVote playerVote = optional.get();

			if (playerVote.getVoteCount() == 0) {
				message(sender, Message.VOTE_REMOVE_ERROR, "%player%", player.getName());
				return;
			}

			playerVote.removeVote();
			message(sender, Message.VOTE_REMOVE_SUCCESS, "%player%", player.getName());
		}, true);
	}

	@Override
	public void voteOffline(UUID uniqueId, String serviceName) {

		Reward reward = this.getRandomReward(RewardType.VOTE);
		IStorage iStorage = this.plugin.getIStorage();

		// We will retrieve the PlayerVote object in asymmetric in the database
		// and execute the vote
		this.plugin.get(uniqueId, playerVote -> {
			Vote vote = playerVote.vote(this.plugin, serviceName, reward, true);
			iStorage.insertVote(playerVote, vote, reward);
		}, false);

	}

}
