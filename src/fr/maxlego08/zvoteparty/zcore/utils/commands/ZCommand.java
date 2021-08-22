package fr.maxlego08.zvoteparty.zcore.utils.commands;

import java.util.function.BiConsumer;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.command.VCommand;

public class ZCommand extends VCommand {

	public ZCommand(ZVotePartyPlugin plugin) {
		super(plugin);
	}

	private BiConsumer<VCommand, ZVotePartyPlugin> command;

	@Override
	public CommandType perform(ZVotePartyPlugin main) {
		
		if (command != null){
			command.accept(this, main);
		}

		return CommandType.SUCCESS;
	}

	public VCommand setCommand(BiConsumer<VCommand, ZVotePartyPlugin> command) {
		this.command = command;
		return this;
	}

	public VCommand sendHelp(String command) {
		this.command = (cmd, main) -> main.getCommandManager().sendHelp(command, cmd.getSender());
		return this;
	}

}
