package fr.maxlego08.zvoteparty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import fr.maxlego08.zvoteparty.api.inventory.Inventory;
import fr.maxlego08.zvoteparty.command.CommandObject;
import fr.maxlego08.zvoteparty.inventory.ZInventoryManager;
import fr.maxlego08.zvoteparty.loader.RewardLoader;
import fr.maxlego08.zvoteparty.save.Config;
import fr.maxlego08.zvoteparty.save.Storage;
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
		if (Config.enableVoteMessage)
			message(player, Message.VOTE_INFORMATIONS);

		Inventory inventory = this.plugin.getInventoryManager().getInventory(InventoryName.VOTE);
		Command command = CommandObject.of(inventory);

		ZInventoryManager inventoryManager = this.plugin.getZInventoryManager();
		inventoryManager.createInventory(EnumInventory.INVENTORY_DEFAULT, player, 1, inventory, new ArrayList<>(),
				command);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void vote(String username, String serviceName) {

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

		if (offlinePlayer != null) {

			this.vote(offlinePlayer, serviceName);

		} else
			Logger.info("Impossible to find the player " + username, LogType.WARNING);

		this.handleVoteParty();
	}

	@Override
	public void handleVoteParty() {

		Storage.voteCount++;
		Storage.getInstance().save(this.plugin.getPersist());

	}

	@Override
	public void vote(CommandSender sender, OfflinePlayer player) {
		this.vote(player, "Serveur Minecraft Vote");
		message(sender, Message.VOTE_SEND, "%player%", player.getName());
	}

	@Override
	public void vote(OfflinePlayer offlinePlayer, String serviceName) {
		PlayerVote playerVote = this.plugin.get(offlinePlayer);
		Reward reward = this.getRandomReward();
		playerVote.vote(serviceName, reward);
	}

	@Override
	public Reward getRandomReward() {
		double percent = ThreadLocalRandom.current().nextDouble(0, 100);
		Reward reward = randomElement(this.rewards);
		if (reward.getPercent() <= percent)
			return reward;
		return this.getRandomReward();
	}

	@Override
	public void giveVotes(Player player) {
		PlayerVote playerVote = this.plugin.get(player);
		List<Vote> votes = playerVote.getNeedRewardVotes();
		if (votes.size() > 0) {
			schedule(Config.joinGiveVoteMilliSecond, () -> {
				message(player, Message.VOTE_LATER, "%amount%", votes.size());
				votes.forEach(e -> e.giveReward(player));
			});
		}
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
		Optional<PlayerVote> optional = manager.getPlayer(player);
		if (optional.isPresent()) {
			PlayerVote playerVote = optional.get();
			return playerVote.getVoteCount();
		}
		return 0;
	}

}
