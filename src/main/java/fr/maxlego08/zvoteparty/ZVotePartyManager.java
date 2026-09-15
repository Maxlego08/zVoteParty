package fr.maxlego08.zvoteparty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.maxlego08.zvoteparty.api.PlayerManager;
import fr.maxlego08.zvoteparty.api.PlayerVote;
import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.api.Vote;
import fr.maxlego08.zvoteparty.api.VotePartyManager;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.RewardType;
import fr.maxlego08.zvoteparty.api.storage.IStorage;
import fr.maxlego08.zvoteparty.api.storage.Storage;
import fr.maxlego08.zvoteparty.loader.RewardLoader;
import fr.maxlego08.zvoteparty.save.Config;
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
    private String partySound;

    public ZVotePartyManager(ZVotePartyPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void reload(CommandSender sender) {
        try {
            this.plugin.reloadConfig();
            this.loadConfiguration();
            this.plugin.getSavers().forEach(e -> e.load(this.plugin.getPersist()));
            this.plugin.reloadInventories();
            message(sender, Message.RELOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            message(sender, Message.RELOAD_SUCCESS);
        }
    }

    @Override
    public void loadConfiguration() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), "config.yml"));
        ConfigurationSection configurationSection;

        this.rewards.clear();
        Loader<Reward> loader = new RewardLoader();
        try {
            configurationSection = configuration.getConfigurationSection("rewards.");
            if (configurationSection != null) {
                for (String key : configurationSection.getKeys(false)) {
                    String path = "rewards." + key + ".";
                    Reward reward = loader.load(configuration, path);
                    if (reward != null) this.rewards.add(reward);
                }
            }
        } catch (Exception ignored) {}

        Logger.info("Loaded " + this.rewards.size() + " rewards", LogType.SUCCESS);

        this.needVote = configuration.getLong("party.votes_needed", 50);
        this.globalCommands = configuration.getStringList("party.global_commands");
        this.commands = configuration.getStringList("party.commands");
        this.partySound = configuration.getString("party.Sound", "");

        this.partyRewards.clear();
        if (configuration.isConfigurationSection("party.rewards.")) {
            try {
                configurationSection = configuration.getConfigurationSection("party.rewards.");
                for (String key : configurationSection.getKeys(false)) {
                    String path = "party.rewards." + key + ".";
                    Reward reward = loader.load(configuration, path);
                    if (reward != null) this.partyRewards.add(reward);
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void openVote(Player player) {
        if (Config.enableVoteMessage) message(player, Message.VOTE_INFORMATIONS);
        if (Config.enableVoteInventory && this.plugin.getLoader() != null) {
            this.plugin.getLoader().open(player);
            return;
        }
        if (!Config.enableVoteMessage) message(player, "§cError in configuration, please contact an administrator.");
    }

    @Override
    public void vote(String username, String serviceName, boolean updateVoteParty) {
        OfflinePlayer offlinePlayer = Bukkit.getPlayerExact(username);
        if (offlinePlayer == null) offlinePlayer = Bukkit.getOfflinePlayer(username);

        this.handleVoteParty();

        if (offlinePlayer != null) {
            this.vote(offlinePlayer, serviceName);
        } else {
            IStorage iStorage = this.plugin.getIStorage();
            iStorage.performCustomVoteAction(username, serviceName, null);
        }
    }

    @Override
    public void handleVoteParty() {
        IStorage iStorage = this.plugin.getIStorage();
        iStorage.addVoteCount(1);
        if (iStorage.getVoteCount() >= this.needVote) this.start();
    }

    @Override
    public void vote(CommandSender sender, String username, boolean updateVoteParty) {
        this.vote(username, "Serveur Minecraft Vote", updateVoteParty);
        message(sender, Message.VOTE_SEND, "%player%", username);
    }

    @Override
    public void vote(OfflinePlayer offlinePlayer, String serviceName) {
        Reward reward = getRandomReward(RewardType.VOTE);
        if (reward == null) return;

        IStorage iStorage = this.plugin.getIStorage();

        if (reward.needToBeOnline() && Config.storage.equals(Storage.REDIS) && !offlinePlayer.isOnline()) {
            iStorage.performCustomVoteAction(offlinePlayer.getName(), serviceName, offlinePlayer.getUniqueId());
            return;
        }

        this.plugin.get(offlinePlayer, playerVote -> {
            Vote vote = playerVote.vote(this.plugin, serviceName, reward, false);
            iStorage.insertVote(playerVote, vote, reward);
        }, false);
    }

    @Override
    public void secretStart() {
        for (Player player : Bukkit.getOnlinePlayers()) {
    
        boolean eligible = true;
            // Check if only voters should receive rewards
            if (Config.only_voters_rewards) {
                long votes = this.plugin.getPlayerManager().getSyncPlayer(player)
                                .map(PlayerVote::getVoteCount)
                                .orElse(0L);
                if (votes == 0) eligible = false;
            }
    
            if (eligible) {
                // Rewards and global commands for eligible players
                ZVotePartyPlugin.getScheduler().runNextTick(task ->
                    this.globalCommands.forEach(command -> {
                        if (command == null || command.trim().isEmpty()) return;
                        command = command.replace("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.papi(command, player));
                })
                );
    
                if (!this.partySound.isEmpty()) {
                    try {
                        String[] parts = this.partySound.split(":");
                        String soundName = parts[0].toUpperCase();
                        float volume = parts.length > 1 ? Float.parseFloat(parts[1]) : 1.0f;
                        float pitch = parts.length > 2 ? Float.parseFloat(parts[2]) : 1.0f;
                        player.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);
                    } catch (Exception ignored) {}
                }
    
                Reward reward = getRandomReward(RewardType.PARTY);
                if (reward != null) reward.give(this.plugin, player);
    
        } else {
                // Non-voter feedback
                message(player, Message.NOT_ELIGIBLE_PARTY);
                // Optional sound for non-eligible players
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
            }
        }
    
        // Execute party commands for all eligible players
        ZVotePartyPlugin.getScheduler().runNextTick(task ->
            this.commands.forEach(command -> {
                if (command == null || command.trim().isEmpty()) return;
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            })
        );
    
        broadcast(Message.VOTE_PARTY_START);
    }

    @Override
    public Reward getRandomReward(RewardType type) {
        List<Reward> rewardList = type == RewardType.VOTE ? this.rewards : this.partyRewards;
        if (rewardList.isEmpty()) return null;

        int attempts = 0;
        Reward selected = null;
        while (attempts < 10) {
            Reward reward = rewardList.get(ThreadLocalRandom.current().nextInt(rewardList.size()));
            double chance = ThreadLocalRandom.current().nextDouble(0, 100);
            if (reward.getPercent() >= 100 || reward.getPercent() >= chance) {
                selected = reward;
                break;
            }
            attempts++;
        }
        return selected != null ? selected : rewardList.get(ThreadLocalRandom.current().nextInt(rewardList.size()));
    }

    @Override
    public void giveVotes(Player player) {
        this.plugin.get(player, playerVote -> {
            List<Vote> votes = playerVote.getNeedRewardVotes();
            if (!votes.isEmpty()) {
                schedule(Integer.valueOf((int) Config.joinGiveVoteMilliSecond), () -> {
                    message(player, Message.VOTE_LATER, "%amount%", votes.size());
                    votes.forEach(e -> e.giveReward(this.plugin, player));
                });
                this.plugin.getIStorage().updateRewards(player.getUniqueId());
            }
        }, true);
    }

    @Override
    public void save(Persist persist) {}

    @Override
    public void load(Persist persist) {}

    @Override
    public List<String> getGlobalCommands() { return this.globalCommands; }

    @Override
    public List<Reward> getPartyReward() { return this.partyRewards; }

    @Override
    public long getNeedVotes() { return this.needVote; }

    @Override
    public long getPlayerVoteCount(Player player) {
        Optional<PlayerVote> optional = this.plugin.getPlayerManager().getSyncPlayer(player);
        return optional.map(PlayerVote::getVoteCount).orElse(0);
    }

    @Override
    public void sendNeedVote(CommandSender sender) { message(sender, Message.VOTE_NEEDED); }

    @Override
    public void forceStart(CommandSender sender) {
        message(sender, Message.VOTE_STARTPARTY);
        this.start();
    }

    @Override
    public void start() {
        this.plugin.getIStorage().startVoteParty();
        this.secretStart();
    }

    @Override
    public void secretStart() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            ZVotePartyPlugin.getScheduler().runNextTick(task ->
                    this.globalCommands.forEach(command -> {
                        if (command == null || command.trim().isEmpty()) return;
                        command = command.replace("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.papi(command, player));
                    })
            );

            if (!this.partySound.isEmpty()) {
                try {
                    String[] parts = this.partySound.split(":");
                    String soundName = parts[0].toUpperCase();
                    float volume = parts.length > 1 ? Float.parseFloat(parts[1]) : 1.0f;
                    float pitch = parts.length > 2 ? Float.parseFloat(parts[2]) : 1.0f;
                    player.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);
                } catch (Exception ignored) {}
            }

            Reward reward = getRandomReward(RewardType.PARTY);
            if (reward != null) reward.give(this.plugin, player);
        }

        ZVotePartyPlugin.getScheduler().runNextTick(task ->
                this.commands.forEach(command -> {
                    if (command == null || command.trim().isEmpty()) return;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                })
        );

        broadcast(Message.VOTE_PARTY_START);
    }

    @Override
    public void removeVote(CommandSender sender, OfflinePlayer player) {
        this.plugin.getPlayerManager().getPlayer(player, optional -> {
            if (!optional.isPresent() || optional.get().getVoteCount() == 0) {
                message(sender, Message.VOTE_REMOVE_ERROR, "%player%", player.getName());
                return;
            }
            PlayerVote playerVote = optional.get();
            playerVote.removeVote();
            message(sender, Message.VOTE_REMOVE_SUCCESS, "%player%", player.getName());
        }, true);
    }

    @Override
    public void voteOffline(UUID uniqueId, String serviceName) {
        Reward reward = getRandomReward(RewardType.VOTE);
        if (reward == null) return;

        this.plugin.get(uniqueId, playerVote -> {
            Vote vote = playerVote.vote(this.plugin, serviceName, reward, true);
            this.plugin.getIStorage().insertVote(playerVote, vote, reward);
        }, false);
    }
}
