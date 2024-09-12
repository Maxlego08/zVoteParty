package fr.maxlego08.zvoteparty.implementations;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

/**
 * Implementation of Reward that manages commands and messages given as rewards.
 */
public class ZReward extends ZUtils implements Reward {

    private final double percent;
    private final List<String> commands;
    private final boolean needToBeOnline;
    private final List<String> messages;

    /**
     * Constructs a ZReward with the specified parameters.
     *
     * @param percent the percentage value of the reward
     * @param commands the list of commands to be executed
     * @param needToBeOnline whether the reward requires the player to be online
     * @param messages the list of messages to be sent
     */
    public ZReward(double percent, List<String> commands, boolean needToBeOnline, List<String> messages) {
        super();
        this.percent = percent;
        this.commands = commands;
        this.needToBeOnline = needToBeOnline;
        this.messages = messages;
    }

    @Override
    public double getPercent() {
        return percent;
    }

    @Override
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public boolean needToBeOnline() {
        return needToBeOnline;
    }

    @Override
    public void give(Plugin plugin, OfflinePlayer player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            commands.forEach(command -> {
                String formattedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
            });
        });

        Bukkit.getOnlinePlayers().forEach(oPlayer -> {
            messages.forEach(message -> {
                messageWO(oPlayer, papi(message, oPlayer), "%player%", player.getName());
            });
        });
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }
}
