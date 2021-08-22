package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.enums.Message;
import fr.maxlego08.zvoteparty.zcore.enums.Permission;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandReload extends VCommand {

	public CommandReload(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_RELOAD);
		this.addSubCommand("reload", "rl");
		this.setPermission(Permission.ZVOTEPARTY_RELOAD);
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		this.manager.reload(this.sender);
		
		return CommandType.SUCCESS;
	}

}
