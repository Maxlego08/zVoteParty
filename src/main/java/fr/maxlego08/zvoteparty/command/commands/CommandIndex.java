package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandIndex extends VCommand {

	public CommandIndex(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZVOTEPARTY_USE);
		this.addSubCommand(new CommandVersion(plugin));
		this.addSubCommand(new CommandReload(plugin));
		this.addSubCommand(new CommandHelp(plugin));
		this.addSubCommand(new CommandAdd(plugin));
		this.addSubCommand(new CommandRemove(plugin));
		this.addSubCommand(new CommandConfig(plugin));
		this.addSubCommand(new CommandStartParty(plugin));
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		this.manager.sendNeedVote(this.sender);
		
		return CommandType.SUCCESS;
	}

}
