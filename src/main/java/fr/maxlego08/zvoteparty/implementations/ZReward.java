package fr.maxlego08.zvoteparty.implementations;

import java.util.List;
import java.util.stream.Collectors;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.zvoteparty.api.Reward;
import fr.maxlego08.zvoteparty.zcore.utils.ZUtils;

public class ZReward extends ZUtils implements Reward {

    private final double percent;
    private final List<String> commands;
    private final boolean needToBeOnline;
    private final List<String> messages;

    public ZReward(double percent, List<String> commands, boolean needToBeOnline, List<String> messages) {
        super();
        this.percent = percent;
        this.commands = commands;
        this.needToBeOnline = needToBeOnline;
        this.messages = messages;
    }

    @Override
    public double getPercent() {
        return this.percent;
    }

    @Override
    public List<String> getCommands() {
        return this.commands;
    }

    @Override
    public boolean needToBeOnline() {
        return this.needToBeOnline;
    }

    @Override
    public List<String> getMessages() {
        return this.messages;
    }

    @Override
    public void give(Plugin plugin, OfflinePlayer player) {

        if (player == null) {
            Bukkit.getLogger().warning("zVoteParty: Player is null! Cannot give reward.");
            return;
        }

        // Pre-filter commands and messages
        List<String> validCommands = (this.commands == null) ? List.of() :
                this.commands.stream().filter(cmd -> cmd != null && !cmd.trim().isEmpty()).collect(Collectors.toList());

        List<String> validMessages = (this.messages == null) ? List.of() :
                this.messages.stream().filter(msg -> msg != null && !msg.trim().isEmpty()).collect(Collectors.toList());

        // Handle commands and percent warnings
        boolean percentInvalid = this.percent <= 0;

        if (validCommands.isEmpty() && percentInvalid) {
            Bukkit.getLogger().warning("zVoteParty: Reward command list is empty and percent value is not provided! Ignoring it.");
        } else if (validCommands.isEmpty()) {
            Bukkit.getLogger().warning("zVoteParty: Reward command list is empty! Ignoring it.");
        } else if (percentInvalid) {
            Bukkit.getLogger().warning("zVoteParty: Percent value is not provided! Ignoring it.");
        } else {
            ZVotePartyPlugin.getScheduler().runNextTick(task -> 
                validCommands.forEach(command -> 
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()))
                )
            );
        }

        // Handle messages
        if (validMessages.isEmpty()) {
            Bukkit.getLogger().warning("zVoteParty: Reward message list is empty! No messages will be sent to players.");
        } else {
            Bukkit.getOnlinePlayers().forEach(oPlayer -> 
                validMessages.forEach(message -> 
                    this.messageWO(oPlayer, papi(message, oPlayer), "%player%", player.getName())
                )
            );
        }
    }
}
