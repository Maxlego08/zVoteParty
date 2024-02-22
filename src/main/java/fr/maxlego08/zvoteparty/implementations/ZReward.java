package fr.maxlego08.zvoteparty.implementations;

import java.util.List;

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

	/**
	 * @param percent
	 * @param commands
	 * @param needToBeOnline
	 * @param messages
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
	public void give(Plugin plugin, OfflinePlayer player) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			this.commands.forEach(command -> {
				command = command.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			});
		});

		Bukkit.getOnlinePlayers().forEach(oPlayer -> {
			this.messages.forEach(message -> {
				this.messageWO(oPlayer, papi(message, oPlayer), "%player%", player.getName());
			});
		});

	}

	@Override
	public List<String> getMessages() {
		return this.messages;
	}

}
