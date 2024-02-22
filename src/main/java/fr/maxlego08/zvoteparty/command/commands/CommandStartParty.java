package fr.maxlego08.zvoteparty.command.commands;

import fr.maxlego08.zvoteparty.zcore.utils.commands.CommandType;
import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.enums.Message;
import fr.maxlego08.zvoteparty.api.enums.Permission;
import fr.maxlego08.zvoteparty.command.VCommand;

public class CommandStartParty extends VCommand {

	public CommandStartParty(ZVotePartyPlugin plugin) {
		super(plugin);
		this.setDescription(Message.DESCRIPTION_STARTPARTY);
		this.addSubCommand("startparty", "sp");
		this.setPermission(Permission.ZVOTEPARTY_STARTPARTY);	
	}

	@Override
	protected CommandType perform(ZVotePartyPlugin plugin) {
		
		this.manager.forceStart(this.sender);
		
		return CommandType.SUCCESS;
	}

}
