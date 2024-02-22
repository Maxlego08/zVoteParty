package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;
import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;

public class CommandVote extends VCommand {

	public CommandVote(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_HELP);
		this.setPermission(Permission.ZVOTEPARTY_VOTE);
		this.setConsoleCanUse(false);
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {

		this.manager.openVote(this.player);
		
		return CommandType.SUCCESS;
	}

}
