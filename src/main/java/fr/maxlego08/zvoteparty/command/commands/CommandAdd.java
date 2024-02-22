package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;
import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;

public class CommandAdd extends VCommand {

	public CommandAdd(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_ADD);
		this.addSubCommand("add");
		this.setPermission(Permission.ZVOTEPARTY_ADD);
		this.addRequireArg("player");
		this.addOptionalArg("party (true/false)");
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		String player = this.argAsString(0);
		boolean updateVoteParty = this.argAsBoolean(1, false);
		this.plugin.getManager().vote(this.sender, player, updateVoteParty);
		
		return CommandType.SUCCESS;
	}

}
